package com.boot.product.service;

import java.util.ArrayList;
import java.util.List;


import com.boot.product.dto.ProductDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.model.Product;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.repository.ProductRepository;
import com.boot.product.validator.ProductValidator;


import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import static com.boot.product.model.Product.*;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

	private ProductValidator productValidator;

	private ProductRepository productRepository;

	public ProductDTO addProduct(ProductDTO productDTO) throws InvalidInputDataException {
		log.info("addProduct - process started");

		if (productValidator.isProductNamePresent(productDTO.getProductName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}
		productDTO.setStatus(ProductStatus.ACTIVE);

		Product product = productRepository.save(dtoToProductEntity(productDTO));

		return productEntityToDto(product);
	}

	public ProductDTO deleteProductByProductName(String productName) throws EntityNotFoundException {
		if (!productValidator.isProductNamePresent(productName)) {
			throw new EntityNotFoundException(
					"Product with Product Name: " + productName + " not found in the Database!");
		}
		Product product = productRepository.findByProductNameAndStatus(productName,ProductStatus.ACTIVE);
		product.setStatus(ProductStatus.INACTIVE);
		productRepository.save(product);

		log.info("{} - succesfully set as INACTIVE", productName);
		return productEntityToDto(product);
	}

	public List<ProductDTO> findAllProducts(@NotNull List<String> products) throws EntityNotFoundException {

		log.info("findAllProducts - process started");
		List<ProductDTO> productDTOList = new ArrayList<>();

		for (String product : products) {

			productDTOList.add(getProductByProductName(product.replaceAll("(^\\[|\\]$)", "")));
		}
		return productDTOList;
	}

	public ProductDTO getProductByProductName(String productName) throws EntityNotFoundException {

		if (!productValidator.isProductNamePresent(productName)) {
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		Product product = productRepository.findByProductNameAndStatus(productName, ProductStatus.ACTIVE);

		return productEntityToDto(product);
	}

	public ProductDTO getProductByProductNameInactive(String productName) throws EntityNotFoundException {

		if (!productValidator.isProductNamePresent(productName)) {
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		Product product = productRepository.findByProductNameAndStatus(productName, ProductStatus.INACTIVE);

		return productEntityToDto(product);
	}

	public ProductDTO updateProductByProductName(String productName, ProductDTO productDTO)
			throws InvalidInputDataException {

		Product product = productRepository.findByProductName(productName);

		ProductDTO newProductDto = productEntityToDto(product);

		if (product.getProductName().matches(productName)) {
			newProductDto.setProductName(productDTO.getProductName());
		} else if (productValidator.isProductNamePresent(productDTO.getProductName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}
		newProductDto.setProductPhotoLink(productDTO.getProductPhotoLink());

		newProductDto.setProductPrice(productDTO.getProductPrice());

		newProductDto.setProductCategory(productDTO.getProductCategory());

		newProductDto.setProductStock(productDTO.getProductStock());

		productRepository.save(updateDtoToProductEntity(product, newProductDto));

		return productEntityToDto(product);
	}
}
