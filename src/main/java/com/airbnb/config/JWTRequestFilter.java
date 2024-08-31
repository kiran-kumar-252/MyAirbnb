package com.airbnb.config;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PropertyUserRepository userRepo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");    /* This line helps us to get the token,
         which is present in the Authorization of header part of http request. */
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){    /* since, In the Authorization, token
         contains Bearer as the starting word. */
            String token = tokenHeader.substring(8, tokenHeader.length() - 1);  // This line helps us to remove bearer word from the token.
            String username = jwtService.getUsername(token);
            Optional<PropertyUser> optionalUser = userRepo.findByUsername(username);
            if(optionalUser.isPresent()){
                PropertyUser user = optionalUser.get();

                // The below 3 lines help us to keep track of the current user logged-in.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));   // This line creates sessions for users.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);    // This line is to tell spring to continue further required filtrations.
    }
}
