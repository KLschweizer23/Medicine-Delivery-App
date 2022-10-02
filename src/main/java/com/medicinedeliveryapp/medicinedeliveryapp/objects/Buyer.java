package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "buyers")
public @Data class Buyer extends User{

    public Buyer(Long id, String firstname, String lastName, String sex, String email, String password, String address, String role, Long buyer_id, List<Cart> carts) {
        super(id, firstname, lastName, sex, email, password, address, role);
        this.buyer_id = buyer_id;
        this.carts = carts;
    }
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long buyer_id;

    @OneToMany( cascade = CascadeType.ALL )
    private List<Cart> carts;

}
