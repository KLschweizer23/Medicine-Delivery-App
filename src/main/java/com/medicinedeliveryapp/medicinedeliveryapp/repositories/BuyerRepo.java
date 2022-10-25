package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;

public interface BuyerRepo extends JpaRepository<Buyer, Long> {
    
    Buyer findByUser(User user);

}
