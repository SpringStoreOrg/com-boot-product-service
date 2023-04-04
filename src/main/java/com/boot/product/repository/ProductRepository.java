package com.boot.product.repository;

import java.util.List;

import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameAndStatus(String name, ProductStatus status);

    Product findByName(String name);

    List<Product> findByNameInAndStatus(List<String> name,ProductStatus status);

    List<Product> findByNameIn(List<String> name);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategoryAndStatus(String category, ProductStatus status);

    @Query("select new com.boot.product.dto.ProductInfoDTO(name, price, stock) from Product where status ='ACTIVE' and name in (:names)")
    List<ProductInfoDTO> getActiveProductsInfo(@Param("names") List<String> productNames);

    @Query("select new com.boot.product.dto.ProductInfoDTO(name, price, stock) from Product where status ='ACTIVE' and name = :name")
    ProductInfoDTO getActiveProductInfo(@Param("name") String productName);
}
