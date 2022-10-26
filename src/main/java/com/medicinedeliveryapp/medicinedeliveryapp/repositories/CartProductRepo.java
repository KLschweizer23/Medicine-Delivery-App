package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.CartProduct;

public interface CartProductRepo extends JpaRepository<CartProduct, Long> {

}
