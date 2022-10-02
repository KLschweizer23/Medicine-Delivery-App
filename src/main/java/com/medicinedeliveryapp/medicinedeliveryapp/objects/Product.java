package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table( name = "products" )
public @Data class Product {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 255 )
    private String code;

    @Column( nullable = false, length = 255 )
    private String generic_name;

    @Column( nullable = false, length = 255 )
    private String brand_name;

}
