package com.airbnb.controller;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking,
                                                 @AuthenticationPrincipal PropertyUser user,
                                                 @PathVariable long propertyId){

        booking.setPropertyUser(user);
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        Property property = optionalProperty.get();
        int propertyPrice = property.getNightlyPrice();
        int totalNights = booking.getTotalNights();
        int totalPrice = propertyPrice * totalNights;
        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);
        Booking createdBooking = bookingRepository.save(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);


    }
}
