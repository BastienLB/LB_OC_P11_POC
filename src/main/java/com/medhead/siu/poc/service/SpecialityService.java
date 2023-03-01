package com.medhead.siu.poc.service;

import com.medhead.siu.poc.model.Hospital;
import com.medhead.siu.poc.model.Speciality;
import com.medhead.siu.poc.repository.SpecialityRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;

    public Iterable<Speciality> getSpecialities() { return specialityRepository.findAll(); }

//    public Iterable<Hospital> getHospitalsFromSpeciality(Long id) {
//        Optional<Speciality> speciality = specialityRepository.findById(id);
//
//        if (speciality.isPresent()) {
//            return speciality.get().getHospitals();
//        }
//        return null;
//    }
}
