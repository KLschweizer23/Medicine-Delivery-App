package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buyers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buyer{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long buyer_id;

    @Column( nullable = false )
    private double discount;

    @OneToOne( cascade = CascadeType.ALL )
    private Cart cart;
    
    @OneToOne( cascade = CascadeType.ALL )
    private User user;

}
