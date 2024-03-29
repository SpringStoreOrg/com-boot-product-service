package com.boot.product.repository;

import java.util.List;

import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameAndStatus(String name, ProductStatus status);

    Product findByName(String name);

    Product findBySlug(String slug);

    boolean existsBySlug(String slug);

    Product findBySlugAndStatus(String slug, ProductStatus status);

    List<Product> findByNameInAndStatus(List<String> name,ProductStatus status, Pageable pageable);

    List<Product> findBySlugInAndStatus(List<String> name,ProductStatus status, Pageable pageable);

    int countAllByNameInAndStatus(List<String> name,ProductStatus status);

    List<Product> findByNameIn(List<String> name, Pageable pageable);

    List<Product> findBySlugIn(List<String> name, Pageable pageable);

    int countAllByNameIn(List<String> name);

    List<Product> findByStatus(ProductStatus status, Pageable pageable);

    int countAllByStatus(ProductStatus status);

    List<Product> findByCategoryAndStatus(String category, ProductStatus status, Pageable pageable);

    @Query("from Product where lower(name) like :name and status = :status")
    List<Product> findByPartialNameAndStatus(@Param("name") String partialName, @Param("status") ProductStatus status, Pageable pageable);

    int countAllByCategoryAndStatus(String category, ProductStatus status);

    @Query("select count(p) from Product p where lower(p.name) like :name and p.status = :status")
    int countByPartialNameAndStatus(@Param("name") String partialName, @Param("status") ProductStatus status);

    @Query("select name from Product where lower(name) like :name")
    List<String> findByNameContainingIgnoreCase(@Param("name") String productName, Pageable pageable);

    @Query("select new com.boot.product.dto.ProductInfoDTO(slug, name, price, stock) from Product where status ='ACTIVE' and slug in (:slugs)")
    List<ProductInfoDTO> getActiveProductsInfo(@Param("slugs") List<String> productSlugs);

    @Query("select new com.boot.product.dto.ProductInfoDTO(slug, name, price, stock) from Product where status ='ACTIVE' and slug = :slug")
    ProductInfoDTO getActiveProductInfo(@Param("slug") String productSlug);

    @Query("select new com.boot.product.dto.ProductInfoDTO(slug, name, price, stock) from Product where slug in (:slugs)")
    List<ProductInfoDTO> getProductsInfo(@Param("slugs") List<String> productSlugs);

    @Modifying
    @Query("update Product p set p.stock = :stock where p.slug = :slug")
    int updateStock(@Param("slug") String productSlug, @Param("stock") int stock);


}
