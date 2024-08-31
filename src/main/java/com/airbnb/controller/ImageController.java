package com.airbnb.controller;

import com.airbnb.repository.ImagesRepository;
import com.airbnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImagesRepository imageRepo;
    @Autowired
    private PropertyRepository propertyRepo;
}
