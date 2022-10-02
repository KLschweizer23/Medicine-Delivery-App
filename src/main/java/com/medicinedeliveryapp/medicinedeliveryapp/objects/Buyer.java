package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
public @Data class Buyer extends User{

    public Buyer(Long id, String firstname, String lastName, String sex, String email, String password, String role) {
        super(id, firstname, lastName, sex, email, password, role);
    }
    
    private Long buyer_id;
    private List<Cart> carts;

}
