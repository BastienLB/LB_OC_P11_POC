package com.medhead.siu.poc;

import static org.mockito.Mockito.when;

import com.medhead.siu.poc.controller.HospitalController;
import com.medhead.siu.poc.helper.CoordinatesHelper;
import com.medhead.siu.poc.helper.MockHelper;
import com.medhead.siu.poc.model.Hospital;
import com.medhead.siu.poc.service.HospitalService;

import static org.assertj.core.api.Assertions.*;

import com.medhead.siu.poc.service.SpecialityService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HospitalControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockHelper mockHelper;

    @Mock
    private HospitalService hospitalService;

    @Autowired
    private SpecialityService specialityService;

    private HospitalController hospitalController;

    @Autowired
    private CustomProperties customProperties;

    List<Hospital> hospitalsList = new ArrayList<>();

    @BeforeAll
    public void initHospitalController() {
        when(hospitalService.getHospitals()).thenReturn(mockHelper.generateHospitalList());

        hospitalsList = mockHelper.generateHospitalList();
        for(int i=1; i<=5; i++) {
            Hospital hospital = hospitalsList.get(i-1);
            when(hospitalService.getHospitalById(Long.valueOf(i))).thenReturn(Optional.of(hospital));
        }
        for(int i=1; i<=5; i++) {
            Hospital hospital = hospitalsList.get(i-1);
            when(hospitalService.saveHospital(hospital)).thenReturn(hospital);
        }

        hospitalController = new HospitalController(hospitalService, specialityService, new CoordinatesHelper(), customProperties);
        hospitalController.refreshKdTree();
    }

    @BeforeEach
    public void resetHospitalsList() {
        hospitalsList = mockHelper.generateHospitalList();
    }

    @Test
    @DisplayName("System returns all hospitals")
    public void get_allHospitalsInDatabase_returnsJsonContainingAllHospitals() throws Exception {

        List<Hospital> expectedResult = hospitalsList;

        Iterable<Hospital> resultReturned = hospitalController.getHospitals();
        List<Hospital> resultReturnedAsList = new ArrayList<>();
        resultReturned.forEach(resultReturnedAsList::add);

        assert expectedResult.equals(resultReturnedAsList);

    }

    @Test
    @DisplayName("System returns the nearest hospital")
    public void get_nearestHospital_fromGivenCoordinates() throws Exception {
        Hospital expectedResult = hospitalsList.get(0);
        Hospital result = hospitalController.getNearestHospital(51.379997, -0.406042);

        assertThat(result.getId()).isEqualTo(expectedResult.getId());
    }

    @Test
    @DisplayName("System returns nearest hospital with available beds")
    public void get_nearestHospitalWithAvailableBeds_fromGivenCoordinates() throws Exception {
        Hospital expectedResult = hospitalsList.get(2);
        Hospital result = hospitalController.getNearestHospital(51.437195, -2.847193);

        assertThat(result.getId()).isEqualTo(expectedResult.getId());

        // Verify that the available beds are decremented by at least one
        assertThat(result.getAvailableBeds()).isEqualTo(expectedResult.getAvailableBeds()-1);
    }

    @Test
    @DisplayName("System returns error when coordinates are not precise enough")
    public void get_errorWhenCoordinatesAreNotPreciseEnough() throws Exception {
        try {
            hospitalController.getNearestHospital(51.4, -2.8);
            assert false;
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("400 BAD_REQUEST \"Coordinates not precise enough\"");
        }
    }

    @Test
    @DisplayName("System returns error when no hospital is found")
    public void get_errorWhenNoHospitalIsFound() throws Exception {
        try {
            hospitalController.getNearestHospital(-70.53, +70.45);
            assert false;
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("400 BAD_REQUEST \"No Hospital has been found\"");
        }
    }
}
