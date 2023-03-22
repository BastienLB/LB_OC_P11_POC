package com.medhead.siu.poc.service;

import com.medhead.siu.poc.model.Hospital;
import com.medhead.siu.poc.repository.HospitalRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Data
@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Iterable<Hospital> getHospitals() { return hospitalRepository.findAll(); }

    public Optional<Hospital> getHospitalById(Long hospitalId) { return hospitalRepository.findById(hospitalId); }

    public Hospital saveHospital(Hospital hospital) { return hospitalRepository.save(hospital); }
}
