package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "cartproducts" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private int quantity;

    @Column( nullable = false )
    private double fixedPrice;

    @ManyToOne()
    private Product product;

}
