package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;

public interface PharmacistRepo extends JpaRepository<Pharmacist, Long>{
    
}
