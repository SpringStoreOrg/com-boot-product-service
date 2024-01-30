package com.boot.product.repository;

import com.boot.product.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Modifying
    @Query("delete from Image i where i in (select im from Image im where im.name = :name and im.product.slug = :slug)")
    int removeImageBySlugAndName(@Param("slug") String productSlug, @Param("name") String imageName);
}
