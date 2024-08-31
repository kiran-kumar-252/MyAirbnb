package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<String> addReview(
            @PathVariable long propertyId,
            @RequestBody Review review,
            @AuthenticationPrincipal PropertyUser user
    ){
        Optional<Property> optionalProperty = propertyRepo.findById(propertyId);
        Property property = optionalProperty.get();
        Review r = reviewRepo.findReviewByUser(property, user);
        if(r!=null){
            return new ResponseEntity<>("You have already added a review for this property", HttpStatus.BAD_REQUEST) ;
        }
        review.setProperty(property);
        review.setPropertyUser(user);
        reviewRepo.save(review);
        return new ResponseEntity<>("Reveiw added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/userReviews")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal PropertyUser user){
        List<Review> reviews = reviewRepo.findByPropertyUser(user);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
