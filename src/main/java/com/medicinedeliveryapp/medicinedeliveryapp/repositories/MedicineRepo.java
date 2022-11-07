package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Medicine;

public interface MedicineRepo extends JpaRepository<Medicine, Long>{
    
}
