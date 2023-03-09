package com.medhead.siu.poc.controller;

import com.medhead.siu.poc.helper.CoordinatesHelper;
import com.medhead.siu.poc.CustomProperties;
import com.medhead.siu.poc.model.Hospital;
import com.medhead.siu.poc.service.HospitalService;
import com.medhead.siu.poc.service.SpecialityService;
import com.thegeekyasian.geoassist.kdtree.KDTree;
import com.thegeekyasian.geoassist.kdtree.KDTreeObject;
import com.thegeekyasian.geoassist.kdtree.geometry.Point;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HospitalController {
    private final HospitalService hospitalService;

    private final SpecialityService specialityService;

    private final CoordinatesHelper coordinatesHelper;

    private final CustomProperties properties;

    private KDTree<Long, Object> kdTree = new KDTree<>();

    @GetMapping("/")
    public String helloWorld() {
        return "Hello world :)";
    }

    /**
     * For testing purpose only
     * Will refresh the KdTree using test data
     */
    @GetMapping("/refreshKdTree")
    public void refreshKdTree() {
        generateKDTree();
    }


    /**
     * READ - Get all hospitals
     * @return Iterable<Hospital> containing all hospitals
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
            if (hospital.getLatitude() != 0.000000 && hospital.getLongitude() != 0.000000 && hospital.getAvailableBeds() > 0) {
                newKdTree.insert(
                    new KDTreeObject.Builder<Long, Object>()
                        .id(hospital.getId())
                        .latitude(hospital.getLatitude())
                        .longitude(hospital.getLongitude())
                        .build()
                );
            }
        }

        this.kdTree = newKdTree;
    }


    /**
     * Find the nearest hospital searching from a K-d Tree
     * @param latitude - User's actual latitude
     * @param longitude - User's actual longitude
     * @return the nearest hospital from the given coordinates
     */
    @GetMapping("/hospitals/{latitude}/{longitude}")
    public Hospital getNearestHospital(
            @PathVariable("latitude") final Double latitude,
            @PathVariable("longitude") final Double longitude
            ) {
        try {

            // Verify if coordinates are precise enough
            if(BigDecimal.valueOf(latitude).scale() < 2 || BigDecimal.valueOf(longitude).scale() < 2) {
                throw new Exception("Coordinates not precise enough");
            }

            Point point = new Point.Builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            List<KDTreeObject<Long, Object>> nearestHospitals = kdTree.findNearestNeighbor(point, properties.getHospitalSearchDistance());
            List<Hospital> hospitals = new ArrayList<>();

            for (KDTreeObject<Long, Object> h : nearestHospitals) {
                Optional<Hospital> hospital = hospitalService.getHospitalById(h.getId());

                hospital.ifPresent(hospitals::add);
            }

            Hospital hospital = coordinatesHelper.findNearestHospital(latitude, longitude, hospitals);
            if(hospital.getId() == null) {
                throw new Exception("No Hospital has been found");
            }
            return hospital;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e
            );
        }
    }
}
