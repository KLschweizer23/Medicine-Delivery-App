package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;

@Entity
@Table( name = "users" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

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
