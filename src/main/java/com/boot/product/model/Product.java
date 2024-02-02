package com.boot.product.model;

import com.boot.product.enums.ProductStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "product")
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
    @Size(min = 3, max = 1000)
    private String description;

    @Column(nullable = false)
    private long price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Image> images = new ArrayList<>();

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

    @OneToOne(mappedBy = "product")
    private Characteristics characteristics;

    public void subtractItems(int quantity) {
        this.stock -= quantity;
    }

    public void addItems(int quantity) {
        this.stock += quantity;
    }

    private String getState() {
        if (this.getCreatedOn().isAfter(LocalDateTime.now().minusDays(10))) {
            return "New";
        }

        return null;
    }

    @PrePersist
    public void beforeInsert() {
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.lastUpdatedOn = LocalDateTime.now();
    }

}

