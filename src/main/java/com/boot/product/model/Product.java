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
	private String productName;

	@Column
	@Size(min = 3, max = 600)
	private String productDescription;

	@Column
	private long productPrice;

	@Column
	private String productPhotoLink;

	@Column
	@Size(min = 3, max = 30)
	private String productCategory;

	@Column
	private int productStock;

	@Enumerated(EnumType.STRING)
	@Column
	private ProductStatus status;


	public static ProductDTO productEntityToDto(Product product) {
		return new ProductDTO()
				.setId(product.getId())
				.setProductName(product.getProductName())
				.setProductDescription(product.getProductDescription())
				.setProductPrice(product.getProductPrice())
				.setProductPhotoLink(product.getProductPhotoLink())
				.setProductCategory(product.getProductCategory())
				.setProductStock(product.getProductStock())
				.setStatus(product.getStatus());
	}

	public static Product dtoToProductEntity(ProductDTO productDto) {
		return new Product().setId(productDto.getId())
				.setProductName(productDto.getProductName())
				.setProductDescription(productDto.getProductDescription())
				.setProductPrice(productDto.getProductPrice())
				.setProductPhotoLink(productDto.getProductPhotoLink())
				.setProductCategory(productDto.getProductCategory())
				.setProductStock(productDto.getProductStock())
				.setStatus(productDto.getStatus());
	}

	public static Product updateDtoToProductEntity(Product product, ProductDTO productDto) {
		return product.setId(productDto.getId())
				.setProductName(productDto.getProductName())
				.setProductDescription(productDto.getProductDescription())
				.setProductPrice(productDto.getProductPrice())
				.setProductPhotoLink(productDto.getProductPhotoLink())
				.setProductCategory(productDto.getProductCategory())
				.setProductStock(productDto.getProductStock())
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
							.setProductName(k.getProductName())
							.setProductDescription(k.getProductDescription())
							.setProductPrice(k.getProductPrice())
							.setProductPhotoLink(k.getProductPhotoLink())
							.setProductCategory(k.getProductCategory())
							.setProductStock(k.getProductStock()))
					.setQuantity(v)
					.setProductTotalPrice(k.getProductPrice() * v));
		});

		Collections.sort(productsInCartList, new Comparator<ProductInCartDTO>() {
			@Override
			public int compare(ProductInCartDTO o1, ProductInCartDTO o2) {
				return o1.getProductDto().getProductName().compareTo(o2.getProductDto().getProductName());
			}
		});

		return productsInCartList;
	}


	public static List<Product> dtoToProductEntityMap(List<ProductInCartDTO> productInCartDTOList) {

		List<Product> productList = new ArrayList<>();

		productInCartDTOList.forEach(pDTO -> productList.add(dtoToProductEntity(pDTO.getProductDto())));

		return productList;
	}


}

