package com.boot.product.repository;

import com.boot.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductCategoryRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);
}
