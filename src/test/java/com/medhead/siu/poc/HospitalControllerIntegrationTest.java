package com.medhead.siu.poc;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.*;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
        scripts = "/data_test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class HospitalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void resetKdTree() throws Exception {
        mockMvc.perform(get("/refreshKdTree")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("System returns all hospitals")
    public void get_allHospitalsInDatabase_returnsJsonContainingAllHospitals() throws Exception {
        MvcResult result = mockMvc.perform(get("/hospitals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Walton Community Hospital - Virgin Care Services Ltd")))
                .andExpect(jsonPath("$[4].id", is(5)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONArray contentAsJson = new JSONArray(content);

        assertThat(contentAsJson.length()).isEqualTo(5);
    }

    @Test
    @DisplayName("System returns the nearest hospital")
    public void get_nearestHospital_fromGivenCoordinates() throws Exception {
        mockMvc.perform(get("/hospitals/51.379997/-0.406041"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.availableBeds", is(8)));
    }

    @Test
    @DisplayName("System returns nearest hospital with available beds")
    public void get_nearestHospitalWithAvailableBeds_fromGivenCoordinates() throws Exception {
        mockMvc.perform(get("/hospitals/51.437195/-2.847193"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @DisplayName("System returns error when coordinates are not precise enough")
    public void get_errorWhenCoordinatesAreNotPreciseEnough() throws Exception {
        mockMvc.perform(get("/hospitals/51.4/-2.8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("System returns error when no hospital is found")
    public void get_errorWhenNoHospitalIsFound() throws Exception {
        mockMvc.perform(get("/hospitals/-70.12/+70.12"))
                .andExpect(status().isBadRequest());
    }
}
