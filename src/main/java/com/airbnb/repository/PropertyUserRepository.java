package com.airbnb.repository;

import com.airbnb.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyUserRepository extends JpaRepository<PropertyUser, Long> {
    Optional<PropertyUser> findByUsername(String username);    /* Here, We create an Optional class becuase it
        provides a feature to avoid Null pointer exception, which we utilized in service layer. */


}