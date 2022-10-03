package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Long>{
    
}
