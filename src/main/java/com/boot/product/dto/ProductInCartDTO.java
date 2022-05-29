package com.boot.product.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCartDTO {

	private ProductDTO productDto;

	private int quantity;
	
	private long totalPrice;
}
