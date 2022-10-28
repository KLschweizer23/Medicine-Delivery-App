package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {

}
