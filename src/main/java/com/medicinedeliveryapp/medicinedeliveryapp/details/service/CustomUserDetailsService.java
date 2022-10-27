package com.medicinedeliveryapp.medicinedeliveryapp.details.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.medicinedeliveryapp.medicinedeliveryapp.details.CustomUserDetails;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.UserRepo;

public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        
        if(user == null){
            throw new UsernameNotFoundException("User not found!");
        }

        return new CustomUserDetails(user);

    }

}
