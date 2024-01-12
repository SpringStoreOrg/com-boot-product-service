package com.boot.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDTO extends ProductDTO{
    private int stock;
    private CharacteristicsDTO characteristics;
    private String description;
    private List<String> images;
    private String category;
}
