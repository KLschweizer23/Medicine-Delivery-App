package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "drivers" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long driver_id;

    @Column(nullable = false, length = 255)
    private String license_id;

    @ManyToMany( cascade = CascadeType.ALL )
    private List<DeliveryNotification> deliveryNotifications;

    @OneToOne( cascade = CascadeType.ALL )
    private User user;

}
