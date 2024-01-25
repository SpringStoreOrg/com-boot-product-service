package com.boot.product.dto;


import com.boot.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {

	private long id;

	@NotNull
	@Size(min = 3, message = "Min Product name size is 3 characters!")
	@Size(max = 50, message = "Max Product name size is 50 characters!")
	private String name;

	@NotNull
	@Size(min = 3, message = "Min Slug size is 3 characters!")
	@Size(max = 50, message = "Max Slug size is 50 characters!")
	private String slug;

	@NotNull
	@Size(min = 3,message = "Min Product description size is 3 characters!")
	@Size(max = 1000,message = "Max Product description size is 600 characters!")
	private String description;

	@NotNull
	@Positive(message = "Product price should be positive number!")
	private long price;

	private List<String> images;

	private String thumbnail;

	@NotNull
	@Size(min = 3, message = "Min category name size is 3 characters!")
	@Size(max = 30, message = "Max category name size is 30 characters!")
	private String category;

	@Positive(message = "Product stock should be positive number!")
	private int stock;

	@Valid
	@NotNull
	private CharacteristicsDTO characteristics;

	@Enumerated(EnumType.STRING)
	@Column
	private ProductStatus status;
	private String state;
}