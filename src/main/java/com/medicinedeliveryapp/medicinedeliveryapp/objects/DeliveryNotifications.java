package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table( name = "notifications" )
public @Data class DeliveryNotifications {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 255)
    private String details;

    @Column( nullable = false, length = 255)
    private String receiver;

    @Column( nullable = false, length = 255)
    private String address;

    @Column( nullable = false)
    private boolean done;

}
