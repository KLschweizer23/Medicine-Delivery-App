package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 255 )
    private String fullName;

    @Column( nullable = false, length = 255 )
    private String sex;

    @Column( nullable = false )
    private int age;

    @Column( nullable = false, length = 255 )
    private String address;

    @Column( nullable = false, length = 255 )
    private String phoneNumber;

    @Column( nullable = false, length = 255 )
    private String diagnosis;

    @ManyToMany
    private List<Medicine> medicines;

    @Column( nullable = true )
    private double price;

    @Column( nullable = true )
    private double shippingFee;

}
