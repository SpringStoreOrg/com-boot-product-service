package com.boot.product.model;

import com.boot.product.dto.ProductDTO;
import com.boot.product.enums.ProductStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@Accessors(chain = true)
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

	@Column(unique = true)
	@Size(min = 3, max = 30)
	private String name;

	@Column
	@Size(min = 3, max = 600)
	private String description;

	@Column
	private long price;

	@Column
	private String photoLink;

	@Column
	@Size(min = 3, max = 30)
	private String category;

	@Column
	private int stock;

	@Column
	private int reserved;

	@Enumerated(EnumType.STRING)
	@Column
	private ProductStatus status;

	public int getAvailable(){
		return stock - reserved;
	}

	public void buyQuantity(int quantity){
		this.reserved = this.reserved - quantity;
		this.stock = this.stock - quantity;
	}

	public void reserve(int quantity){
		this.reserved = this.reserved + quantity;
	}

	public void reverseReserve(int quantity){
		this.reserved = this.reserved - quantity;
	}


	public static ProductDTO productEntityToDto(Product product) {
		return new ProductDTO()
				.setId(product.getId())
				.setName(product.getName())
				.setDescription(product.getDescription())
				.setPrice(product.getPrice())
				.setPhotoLink(product.getPhotoLink())
				.setCategory(product.getCategory())
				.setStock(product.getStock())
				.setStatus(product.getStatus());
	}

	public static Product dtoToProductEntity(ProductDTO productDto) {
		return new Product().setId(productDto.getId())
				.setName(productDto.getName())
				.setDescription(productDto.getDescription())
				.setPrice(productDto.getPrice())
				.setPhotoLink(productDto.getPhotoLink())
				.setCategory(productDto.getCategory())
				.setStock(productDto.getStock())
				.setStatus(productDto.getStatus());
	}

	public static Product updateDtoToProductEntity(Product product, ProductDTO productDto) {
		return product.setId(productDto.getId())
				.setName(productDto.getName())
				.setDescription(productDto.getDescription())
				.setPrice(productDto.getPrice())
				.setPhotoLink(productDto.getPhotoLink())
				.setCategory(productDto.getCategory())
				.setStock(productDto.getStock())
				.setStatus(productDto.getStatus());
	}

	public static List<ProductDTO> productEntityToDtoList(List<Product> productList) {

		List<ProductDTO> productDTOList = new ArrayList<>();

		productList.forEach(p -> productDTOList.add(productEntityToDto(p)));

		return productDTOList;
	}

}

