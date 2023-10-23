package com.boot.product.model;

import com.boot.product.dto.ProductDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.util.ProductUtil;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Accessors(chain = true)
@Entity
@Table(name = "product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5714267227877816930L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50)
    private String slug;

    @Column(nullable = false)
    @Size(min = 3, max = 600)
    private String description;

    @Column(nullable = false)
    private long price;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Photo> entries;

    @Column(nullable = false)
    @Size(min = 3, max = 30)
    private String category;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime lastUpdatedOn;

    public void subtractItems(int quantity){
        this.stock-=quantity;
    }

    public void addItems(int quantity){
        this.stock+=quantity;
    }

    public static ProductDTO productEntityToDto(Product product) {
        return new ProductDTO()
                .setId(product.getId())
                .setName(product.getName())
                .setSlug(product.getSlug())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setImages(product.getEntries() != null
                        ? product.getEntries().stream()
                        .map(Photo::getLink)
                        .collect(Collectors.toList())
                        : List.of())
                .setThumbnail(product.getEntries().stream().findFirst().get().getLink())
                .setCategory(product.getCategory())
                .setStock(product.getStock())
                .setStatus(product.getStatus())
                .setState(product.getState())
                .setCreatedOn(product.getCreatedOn())
                .setLastUpdatedOn(product.getLastUpdatedOn());
    }

    public static Product dtoToProductEntity(ProductDTO productDto) {
        Product product = new Product();

        product.setId(productDto.getId())
                .setName(productDto.getName())
                .setSlug(ProductUtil.getSlug(productDto.getName()))
                .setDescription(productDto.getDescription())
                .setPrice(productDto.getPrice())
                .setEntries(productPhotoLinksToEntries(productDto.getImages(), product))
                .setCategory(productDto.getCategory())
                .setStock(productDto.getStock())
                .setStatus(productDto.getStatus());

        return product;
    }

    public static Product updateDtoToProductEntity(Product product, ProductDTO productDto) {
        return product.setId(productDto.getId())
                .setName(productDto.getName())
                .setSlug(ProductUtil.getSlug(productDto.getName()))
                .setDescription(productDto.getDescription())
                .setPrice(productDto.getPrice())
                .setEntries(productPhotoLinksToEntries(productDto.getImages(), product))
                .setCategory(productDto.getCategory())
                .setStock(productDto.getStock())
                .setStatus(productDto.getStatus());
    }


    public static List<ProductDTO> productEntityToDtoList(List<Product> productList) {

        List<ProductDTO> productDTOList = new ArrayList<>();

        productList.forEach(p -> productDTOList.add(productEntityToDto(p)));

        return productDTOList;
    }

    public static List<Photo> productPhotoLinksToEntries(List<String> photoLinks, Product product) {

        List<Photo> photoLinkEntries = new ArrayList<>();

        photoLinks.forEach(photoLink -> photoLinkEntries.add(new Photo().setLink(photoLink).setProduct(product)));

        return photoLinkEntries;
    }

    private String getState() {
        if (this.getCreatedOn().isAfter(LocalDateTime.now().minusDays(10))) {
            return "New";
        }

        return null;
    }

    @PrePersist
    public void beforeInsert(){
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate(){
        this.lastUpdatedOn = LocalDateTime.now();
    }

}

