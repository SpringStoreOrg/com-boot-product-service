package com.boot.product.model;

import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductInCartDTO;
import com.boot.product.enums.ProductStatus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;



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

	@Enumerated(EnumType.STRING)
	@Column
	private ProductStatus status;


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

	public static List<ProductInCartDTO> productEntityToDtoMap(List<Product> productList) {

		Map<ProductDTO, Integer> productDTOMap = new HashMap<>();

		for (Product p : productList) {
			ProductDTO prod = productEntityToDto(p);
			if (productDTOMap.containsKey(prod)) {
				Integer value = productDTOMap.get(prod) + 1;
				productDTOMap.put(productEntityToDto(p), value);
			} else {
				productDTOMap.put(productEntityToDto(p), 1);
			}
		}

		List<ProductInCartDTO> productsInCartList = new ArrayList<>();

		productDTOMap.forEach((k, v) -> {

			productsInCartList.add(new ProductInCartDTO()
					.setProductDto(new ProductDTO()
							.setStatus(k.getStatus())
							.setId(k.getId())
							.setName(k.getName())
							.setDescription(k.getDescription())
							.setPrice(k.getPrice())
							.setPhotoLink(k.getPhotoLink())
							.setCategory(k.getCategory())
							.setStock(k.getStock()))
					.setQuantity(v)
					.setTotalPrice(k.getPrice() * v));
		});

		Collections.sort(productsInCartList, new Comparator<ProductInCartDTO>() {
			@Override
			public int compare(ProductInCartDTO o1, ProductInCartDTO o2) {
				return o1.getProductDto().getName().compareTo(o2.getProductDto().getName());
			}
		});

		return productsInCartList;
	}


	public static List<Product> dtoToProductEntityMap(List<ProductInCartDTO> productInCartDTOList) {

		List<Product> productList = new ArrayList<>();

		productInCartDTOList.forEach(pDTO -> productList.add(dtoToProductEntity(pDTO.getProductDto())));

		return productList;
	}

	public static List<ProductDTO> productEntityToDtoList(List<Product> productList) {

		List<ProductDTO> productDTOList = new ArrayList<>();

		productList.forEach(p -> productDTOList.add(productEntityToDto(p)));

		return productDTOList;
	}

}

