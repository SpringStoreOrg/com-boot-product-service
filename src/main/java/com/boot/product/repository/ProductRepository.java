package com.boot.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.services.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product getProductById(long id);

    Product findByProductName(String productName);

    List<Product> findByProductCategory(String productCategory);

    void deleteByProductName(String productName);
}
