package com.medicinedeliveryapp.medicinedeliveryapp.details;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractDetails implements UserDetails{

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    public abstract String getPassword();

    public abstract String getUsername();

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public abstract String getFirstName();
    public abstract String getLastName();
    public abstract String getSex();
    public abstract String getEmail();
    public abstract String getAddress();
    public abstract String getRole();
    
}
