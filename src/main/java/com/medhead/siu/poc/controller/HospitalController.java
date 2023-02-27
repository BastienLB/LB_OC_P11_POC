package com.medhead.siu.poc.controller;

import com.medhead.siu.poc.CustomProperties;
import com.medhead.siu.poc.model.Hospital;
import com.medhead.siu.poc.service.HospitalService;
import com.medhead.siu.poc.service.SpecialityService;
import com.thegeekyasian.geoassist.kdtree.KDTree;
import com.thegeekyasian.geoassist.kdtree.KDTreeObject;
import com.thegeekyasian.geoassist.kdtree.geometry.Point;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private CustomProperties properties;

    private KDTree<Long, Object> kdTree = new KDTree<>();


    /**
     * READ - Get all hospitals
     * @return Iterable containing all hospitals
     */
    @GetMapping("/hospitals")
    public Iterable<Hospital> getHospitals() { return hospitalService.getHospitals(); }


    /**
     * Generate the kd-tree which will be used to find the nearest hospital
     * Function is executed on application context startup but can be called at any moment.
     */
    @PostConstruct
    private void generateKDTree() {
        // Initiate the new KdTree
        KDTree<Long, Object> newKdTree = new KDTree<>();

        // Fill the new KdTree
        for(Hospital hospital: hospitalService.getHospitals()) {
            if (hospital.getLatitude() != "" && hospital.getLongitude() != "") {
                newKdTree.insert(
                    new KDTreeObject.Builder<Long, Object>()
                        .id(hospital.getId())
                        .latitude(Double.parseDouble(hospital.getLatitude()))
                        .longitude(Double.parseDouble(hospital.getLongitude()))
                        .build()
                );
            }
        }

        kdTree = newKdTree;
    }


    /**
     * Find all hospitals that provides a particular speciality
     * @param id - ID of the speciality
     * @return Iterable containing all corresponding hospitals
     */
    @GetMapping("/hospitals/speciality/{id}")
    public Iterable<Hospital> getHospitalsBySpeciality(@PathVariable("id") final Long id) {
        return specialityService.getHospitalsFromSpeciality(id);
    }


    /**
     * Find the nearest hospital
     * @param latitude - User actual latitude
     * @param longitude - User actual longitude
     * @return the nearest hospital from the given coordinates
     */
    @GetMapping("/hospitals/{latitude}/{longitude}")
    public Hospital getNearestHospital(
            @PathVariable("latitude") final Float latitude,
            @PathVariable("longitude") final Float longitude
            ) {

        Point point = new Point.Builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        List<KDTreeObject<Long, Object>> nearestHospitals = kdTree.findNearestNeighbor(point, properties.getHospitalSearchDistance());

        Long hospitalId = nearestHospitals.get(0).getId();
        Optional<Hospital> nearestHospital = hospitalService.getHospitalById(hospitalId);

        return nearestHospital.orElse(null);
    }
}
