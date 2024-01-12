package com.boot.product.dto;

import com.boot.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id;
    private String name;

    private String slug;
    private long price;
    private String thumbnail;
    private ProductStatus status;
    private String state;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
