package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;

@Entity
@Table( name = "transactions" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @ManyToOne( cascade = CascadeType.ALL )
    private User user;
    
    @Column( nullable = false, length = 255 )
    private String address;

    @Column( nullable = false, length = 255 )
    private String paymentMethod;

    @Column( nullable = false )
    private double shippingTotal;

    @Column( nullable = false )
    private double discount;

    @ManyToOne( cascade = CascadeType.ALL )
    private OrderList orderList;

    @Column( nullable = false )
    private LocalDateTime dateTransaction;

    @Column( nullable = false )
    private String deliveryStatus;

}
