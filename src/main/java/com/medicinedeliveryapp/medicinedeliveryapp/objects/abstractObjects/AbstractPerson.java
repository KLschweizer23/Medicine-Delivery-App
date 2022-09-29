package com.medicinedeliveryapp.medicinedeliveryapp.objects.abstractObjects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

import javax.persistence.GenerationType;

@MappedSuperclass
public @Data class AbstractPerson {
    
    public AbstractPerson(Long id, String firstname, String lastName, String sex, String email, String password, String role){
        this.id = id;
        this.firstName = firstname;
        this.lastName = lastName;
        this.sex = sex;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, length = 6)
    private String sex;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String role;

}
