package com.boot.product.repository;

import java.util.List;

import com.boot.product.enums.ProductStatus;
import com.boot.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameAndStatus(String name, ProductStatus status);

    Product findByName(String name);

    List<Product> findByCategory(String category);
}
