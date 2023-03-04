package com.boot.product.repository;

import com.boot.product.model.PhotoLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PhotoLinkRepository extends JpaRepository<PhotoLink, Long> {

    PhotoLink findByPhotoLink(String photoLink);

}