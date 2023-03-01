package com.medhead.siu.poc.helper;

import com.medhead.siu.poc.model.Hospital;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoordinatesHelper {

    /**
     * Round a number to 5 digits
     * @param number - the number to round
     * @return double - the number rounded
     */
    public double roundDouble(Double number) {
        return Math.round(number * 10000000d) / 10000000d;
    }

    /**
     * Calculate the distance of a hospital from given coordinates
     * @param latitude - current user latitude
     * @param longitude - current user longitude
     * @param hospital - the hospital we calculate the distance from the given coordinates
     * @return double - the distance of the hospital from the given coordinates
     */
    public double calculateDistance(double latitude, double longitude, Hospital hospital) {
        double latitudeDistance = Math.pow(latitude-hospital.getLatitude(), 2);
        double longitudeDistance = Math.pow(longitude-hospital.getLongitude(), 2);
        return roundDouble(Math.sqrt(latitudeDistance+longitudeDistance));
    }

    /**
     * Find the nearest hospital of a list from the given coordinates
     * @param latitude - current user latitude
     * @param longitude - current user longitude
     * @param hospitals - list of hospitals from which we need to find the nearest
     * @return Hospital - the nearest hospital
     */
    public Hospital findNearestHospital(Double latitude, Double longitude, List<Hospital> hospitals) {
        Hospital nearestHospital = new Hospital();
        double hospitalDistance = -1;
        double challengingHospitalDistance;

        for(Hospital h: hospitals) {
            if (hospitalDistance == -1) {
                nearestHospital = h;
                hospitalDistance = calculateDistance(latitude, longitude, h);
            } else {
              challengingHospitalDistance = calculateDistance(latitude, longitude, h);
              if(challengingHospitalDistance < hospitalDistance) {
                  nearestHospital = h;
                  hospitalDistance = challengingHospitalDistance;
              }
            }
        }
        return nearestHospital;
    }
}
