package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Transaction;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByDeliveryStatus(String status);

    List<Transaction> findAllByDeliveryStatusAndUser(String status, User user);

}
