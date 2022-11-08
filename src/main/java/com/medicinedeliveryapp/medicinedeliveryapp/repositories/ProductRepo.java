package com.medicinedeliveryapp.medicinedeliveryapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
    
    List<Product> findAllByGenericNameContaining(String genericName);

    List<Product> findAllByBrandNameContaining(String brandName);

    List<Product> findAllByBrandNameContainingOrGenericNameContaining(String brandName, String genericName);

}
