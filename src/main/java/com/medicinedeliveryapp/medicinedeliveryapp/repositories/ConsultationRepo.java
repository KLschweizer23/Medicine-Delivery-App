package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Consultation;

public interface ConsultationRepo extends JpaRepository<Consultation, Long>{
    
    List<Consultation> findAllByStatus(String status);

}
