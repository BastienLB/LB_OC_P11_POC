package com.medhead.siu.poc.helper;

import com.medhead.siu.poc.model.Hospital;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockHelper {

    private List<Double> latitudeList = new ArrayList<>() {
        {
            add(51.379997);
            add(51.315132);
            add(51.439693);
            add(51.437195);
            add(53.459743);
        }
    };

    private List<Double> longitudeList = new ArrayList<>() {
        {
            add(-0.406042);
            add(-0.556289);
            add(-2.840069);
            add(-2.847193);
            add(-2.245469);
        }
    };

    private List<Integer> availableBedsList = new ArrayList<>() {
        {
            add(9);
            add(1);
            add(8);
            add(0);
            add(10);
        }
    };


    /**
     * Generate a short hospital list from fixed datas
     * @return ArrayList - generated list of hospitals
     */
    public List<Hospital> generateHospitalList() {
        List<Hospital> hospitalList = new ArrayList<>();

        for(int i=1; i<=5; i++) {
            Hospital h = new Hospital();
            h.setId((long) i);
            h.setName(String.format("Hospital %s", i));
            h.setLatitude(latitudeList.get(i-1));
            h.setLongitude(longitudeList.get(i-1));
            h.setAvailableBeds(availableBedsList.get(i-1));

            hospitalList.add(h);
        }
        return hospitalList;
    }

}
