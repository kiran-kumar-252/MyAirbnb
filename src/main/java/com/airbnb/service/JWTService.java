package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    private Algorithm algorithm;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private int expiryTime;

    private final static String USER_NAME="username";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);  /* as HMAC256() is in italic, it is a static
        method, so we have to call it using class name (here, Algorithm). */
    }

    public String generateToken (PropertyUser propertyUser){
        return JWT.create().
                withClaim(USER_NAME, propertyUser.getUsername()).   /* Here, 'withClaim'(which takes username)
                 takes values as key-value pair, so we create a variable named 'USER_NAME' for the key part of
                 'withclaim' */
                withExpiresAt(new Date(System.currentTimeMillis()+expiryTime)).
                withIssuer(issuer).
                sign(algorithm);
    }
    public String getUsername(String token){
        DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);    /* Remember,
         "rwbv"(rosy with bony v) i.e., short form to remember this line.In this line the verification of
          algorithm, secret key, issuer and expiry time happens and the token is decrypted. */
        return decodedJwt.getClaim(USER_NAME).asString();   // This line gets the username and returns it.
    }
}
