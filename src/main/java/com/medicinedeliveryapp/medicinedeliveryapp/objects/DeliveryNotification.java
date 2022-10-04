package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "notifications" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNotification {
    
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
