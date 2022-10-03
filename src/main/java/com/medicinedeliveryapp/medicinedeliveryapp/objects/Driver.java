package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table( name = "drivers" )
public @Data class Driver extends User {
    
    public Driver(Long id, String firstname, String lastName, String sex, String email, String password, String address, String role, Long role_id, Long driver_id, List<DeliveryNotification> deliveryNotifications) {
        super(id, firstname, lastName, sex, email, password, address, role, role_id);
        this.driver_id = driver_id;
        this.deliveryNotifications = deliveryNotifications;
    }
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long driver_id;

    @ManyToMany( cascade = CascadeType.ALL )
    private List<DeliveryNotification> deliveryNotifications;

}
