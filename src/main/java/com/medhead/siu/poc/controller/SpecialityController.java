package com.medhead.siu.poc.controller;

import com.medhead.siu.poc.model.Speciality;
import com.medhead.siu.poc.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialityController {

    @Autowired
    private SpecialityService specialityService;

    @GetMapping("/specialities")
    public Iterable<Speciality> getSpecialities() { return specialityService.getSpecialities(); }

}
