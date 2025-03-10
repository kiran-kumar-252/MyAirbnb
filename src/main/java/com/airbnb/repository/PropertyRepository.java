package com.airbnb.repository;

import com.airbnb.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("select p from Property p JOIN Location l on p.location = l.id Join Country c on p.country = c.id where l.locationName=:locationName or c.countryName=:locationName")
    List<Property> findByPropertyLocation(@Param("locationName") String locationName);
}