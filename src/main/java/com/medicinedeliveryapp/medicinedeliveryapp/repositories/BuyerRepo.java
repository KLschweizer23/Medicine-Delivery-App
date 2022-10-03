package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;

public interface BuyerRepo extends JpaRepository<Buyer, Long> {
    
}
