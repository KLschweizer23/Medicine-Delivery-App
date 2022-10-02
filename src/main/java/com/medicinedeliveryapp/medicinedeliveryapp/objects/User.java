package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import javax.persistence.GenerationType;

@Entity
@Table( name = "users" )
public @Data class User {
    
    public User(Long id, String firstname, String lastName, String sex, String email, String password, String address, String role){
        this.id = id;
        this.firstName = firstname;
        this.lastName = lastName;
        this.sex = sex;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, length = 255 )
    private String firstName;

    @Column( nullable = false, length = 255 )
    private String lastName;

    @Column( nullable = false, length = 6 )
    private String sex;

    @Column( nullable = false, unique = true, length = 255 )
    private String email;

    @Column( nullable = false, length = 255 )
    private String password;

    @Column( nullable = false, length = 255)
    private String address;

    @Column( nullable = false, length = 255 )
    private String role;

}
