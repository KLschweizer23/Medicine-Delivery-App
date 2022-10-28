package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.OrderList;

public interface OrderListRepo extends JpaRepository<OrderList, Long> {

}
