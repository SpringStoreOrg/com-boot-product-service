package com.boot.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		if (productValidator.isNamePresent(productDTO.getName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}
		productDTO.setStatus(ProductStatus.ACTIVE);

		Product product = productRepository.save(dtoToProductEntity(productDTO));

		return productEntityToDto(product);
	}

	public ProductDTO deleteProductByProductName(String productName) throws EntityNotFoundException {
		if (!productValidator.isNamePresent(productName)) {
			throw new EntityNotFoundException(
					"Product with Product Name: " + productName + " not found in the Database!");
		}
		Product product = productRepository.findByNameAndStatus(productName,ProductStatus.ACTIVE);
		product.setStatus(ProductStatus.INACTIVE);
		productRepository.save(product);

		log.info("{} - successfully set as INACTIVE", productName);
		return productEntityToDto(product);
	}

	public List<ProductDTO> findAllProducts(@NotNull String productParam, Boolean includeInactive){

		log.info("findAllProducts - process started");

		List<String> prodList = Stream.of(productParam.split(",", -1)).collect(Collectors.toList());

		List<Product> productList;

		if (includeInactive) {
			productList = productRepository.findByNameIn(prodList);

		} else {
			productList = productRepository.findByNameInAndStatus(prodList, ProductStatus.ACTIVE);
		}

		return productEntityToDtoList(productList);
	}

	public List<ProductDTO> getAllProducts() {

		log.info("getAllProducts - process started");

		List<Product> prodList = productRepository.findByStatus(ProductStatus.ACTIVE);

		return productEntityToDtoList(prodList);
	}

	public ProductDTO getProductByProductName(String productName, Boolean includeInactive) throws EntityNotFoundException {

		log.info("getProductByProductName - process started");

		if (!productValidator.isNamePresent(productName)) {
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		Product product;
		if (includeInactive) {
			product = productRepository.findByName(productName);

		} else {
			product = productRepository.findByNameAndStatus(productName, ProductStatus.ACTIVE);
		}

		return productEntityToDto(product);
	}

	public ProductDTO updateProductByProductName(String productName, ProductDTO productDTO)
			throws InvalidInputDataException {

		Product product = productRepository.findByName(productName);

		ProductDTO newProductDto = productEntityToDto(product);

		if (product.getName().matches(productName)) {
			newProductDto.setName(productDTO.getName());
		} else if (productValidator.isNamePresent(productDTO.getName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}
		newProductDto.setPhotoLink(productDTO.getPhotoLink());

		newProductDto.setPrice(productDTO.getPrice());

		newProductDto.setCategory(productDTO.getCategory());

		newProductDto.setStock(productDTO.getStock());

		productRepository.save(updateDtoToProductEntity(product, newProductDto));

		return productEntityToDto(product);
	}

	public List<ProductDTO> findByCategoryAndStatus(String category) {

		log.info("findByCategoryAndStatus - process started");

		List<Product> productList = productRepository.findByCategoryAndStatus(category, ProductStatus.ACTIVE);

		ArrayList<ProductDTO> productDTOList = new ArrayList<>();

		productList.forEach(p -> productDTOList.add(productEntityToDto(p)));

		return productDTOList;
	}
}
