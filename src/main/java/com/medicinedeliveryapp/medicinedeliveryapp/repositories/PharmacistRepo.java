package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;

public interface PharmacistRepo extends JpaRepository<Pharmacist, Long>{
    
    Pharmacist findByUser(User user);

}
