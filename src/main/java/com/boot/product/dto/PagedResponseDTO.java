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
public class PagedResponseDTO {
    private List<ProductDTO> products;
    private int totalItems;
    private int totalPages;
    private int currentPage;
}
