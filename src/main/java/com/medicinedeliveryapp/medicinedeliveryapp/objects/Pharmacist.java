package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table( name = "pharmacists" )
public @Data class Pharmacist extends User {
    
    public Pharmacist(Long id, String firstname, String lastName, String sex, String email, String password, String address, String role, Long pharmacist_id, String license_id) {
        super(id, firstname, lastName, sex, email, password, address, role);
        this.pharmacist_id = pharmacist_id;
        this.license_id = license_id;
    }
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long pharmacist_id;

    @Column( nullable = false, length = 255)
    private String license_id;

}
