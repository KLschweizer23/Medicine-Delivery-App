package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.DeliveryNotification;

public interface DeliveryNotifcationRepo extends JpaRepository<DeliveryNotification, Long> {
    
}
