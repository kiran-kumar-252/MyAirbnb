package com.airbnb.service;

import com.airbnb.Dto.LoginDto;
import com.airbnb.Dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private PropertyUserRepository userRepo;

    public UserService(PropertyUserRepository userRepo) {   // using constructor based dependency injection to create bean of 'PropertyUserRepository'.
        this.userRepo = userRepo;
    }

    @Autowired
    private JWTService jwtService;

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(propertyUserDto.getPassword()));
        user.setUserRole("ROLE_USER");
        PropertyUser savedUser = userRepo.save(user);
        return savedUser;
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> optionalUser = userRepo.findByUsername(loginDto.getUsername());
        if(optionalUser.isPresent()){   /* Here, isPresent() is the method of Optional class,
                which is used to know if a value or null is present in the variable.    */
            PropertyUser user = optionalUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {    /* Here, 'BCrypt.checkpw'
                decrypts the encrypted password which is coming from the database and checks if the user given
                password matches with the original password from database. */
                return jwtService.generateToken(user);
            }
        }
        return null;
    }
}

    //  https://github.com/kiran-kumar-252/MyAirbnb.git