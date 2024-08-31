package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepo;

    @GetMapping("{locationName}")
    public ResponseEntity<List<Property>> findProperty(@PathVariable String locationName){
        List<Property> properties = propertyRepo.findByPropertyLocation(locationName);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}
