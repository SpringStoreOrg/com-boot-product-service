package com.boot.product.repository;

import java.util.List;

import com.boot.product.enums.ProductStatus;
import com.boot.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductNameAndStatus(String productName, ProductStatus status);

    Product findByProductName(String productName);

    List<Product> findByProductCategory(String productCategory);
}
