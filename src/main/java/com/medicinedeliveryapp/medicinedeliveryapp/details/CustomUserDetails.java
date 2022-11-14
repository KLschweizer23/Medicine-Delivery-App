package com.medicinedeliveryapp.medicinedeliveryapp.details;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;

public class CustomUserDetails extends AbstractDetails{

    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public String getAddress() {
        return user.getAddress();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getRole() {
        return user.getRole();
    }

    @Override
    public String getSex() {
        return user.getSex();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public Long getId(){
        return user.getId();
    }

    @Override
    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    @Override
    public String getBirthday() {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String bday = LocalDate.parse(user.getBirthday()).format(formatters);
        
        return bday;
    }
    
}
