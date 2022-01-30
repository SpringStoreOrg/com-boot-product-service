package com.boot.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.boot.services.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Product getProductById(long id);

	public Product findByProductName(String productName);

	public List<Product> findByProductCategory(String productCategory);

	@Transactional
	public void deleteByProductName(String productName);
}