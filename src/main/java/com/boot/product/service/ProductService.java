package com.boot.product.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.product.client.UserServiceClient;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.repository.ProductRepository;
import com.boot.product.validator.ProductValidator;
import com.boot.services.dto.ProductDTO;
import com.boot.services.dto.UserDTO;
import com.boot.services.mapper.ProductMapper;
import com.boot.services.model.Product;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductValidator productValidator;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserServiceClient userServiceClient;

	public ProductDTO addProduct(ProductDTO productDTO) throws InvalidInputDataException {
		log.info("addProduct - process started");
		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductName(), 3, 30)) {
			throw new InvalidInputDataException("Product Name has to be between 3 and 30 characters long!");
		}

		if (productValidator.isProductNamePresent(productDTO.getProductName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}

		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductDescription(), 3, 600)) {
			throw new InvalidInputDataException("Product Description has to be between 3 and 600 characters long!");
		}

		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductCategory(), 3, 30)) {
			throw new InvalidInputDataException("Product Category has to be between 3 and 30 characters long!");
		}

		Product product = productRepository.save(ProductMapper.DtoToProductEntity(productDTO));

		return ProductMapper.ProductEntityToDto(product);
	}

	public void deleteProductByProductName(String productName) throws EntityNotFoundException {
		if (!productValidator.isProductNamePresent(productName)) {
			throw new EntityNotFoundException(
					"Product with Product Name: " + productName + " not found in the Database!");
		}

		for (UserDTO user : userServiceClient.callGetAllUsers()) {

			Set<ProductDTO> prodList = user.getFavoriteProductList();

			if (prodList != null) {
				Iterator<ProductDTO> iter = prodList.iterator();

				while (iter.hasNext()) {
					ProductDTO p = iter.next();

					if (p.getProductName().matches(productName)) {
						iter.remove();
						userServiceClient.callUpdateUser(user.getEmail(), user);
						log.info(
								productName + " - succesfully deleted from User with Email: " + user.getEmail());
					}
				}
			}
		}

		productRepository.deleteByProductName(productName);
		log.info(productName + " - succesfully deleted from the Database");

	}

	public List<ProductDTO> findAllProducts() {
		return productRepository.findAll().stream().map(p -> ProductMapper.ProductEntityToDto(p)).collect(Collectors.toList());
	}

	public List<ProductDTO> findByProductCategory(String productCategory) {

		List<Product> productList = productRepository.findByProductCategory(productCategory);

		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		productList.stream().forEach(p -> productDTOList.add(ProductMapper.ProductEntityToDto(p)));

		return productDTOList;
	}

	public ProductDTO getProductById(long id) throws EntityNotFoundException {

		Product product = productRepository.getProductById(id);
		if (product == null){
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		return ProductMapper.ProductEntityToDto(product);
	}



	public ProductDTO getProductByProductName(String productName) throws EntityNotFoundException {
		log.info("getProductByProductName - process started");
		if (!productValidator.isProductNamePresent(productName)) {
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		Product product = productRepository.findByProductName(productName);

		return ProductMapper.ProductEntityToDto(product);
	}

	public ProductDTO updateProductByProductName(String productName, ProductDTO productDTO)
			throws InvalidInputDataException {

		Product product = productRepository.findByProductName(productName);

		ProductDTO newProductDto = ProductMapper.ProductEntityToDto(product);

		//TODO you could move these validations at the DTO level using @Size(min = 3, max = 30) and  @Size(min = 3, max =600). to enable dto validation you will need @Valid in the controller method arguments list.
		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductName(), 3, 30)) {
			throw new InvalidInputDataException("Product Name has to be between 3 and 30 characters long!");
		}

		if (product.getProductName().matches(productName)) {
			newProductDto.setProductName(productDTO.getProductName());
		} else if (productValidator.isProductNamePresent(productDTO.getProductName())) {
			throw new InvalidInputDataException("The Selected Product name is already used!");
		}

		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductDescription(), 3, 600)) {
			throw new InvalidInputDataException("Product Description has to be between 3 and 600 characters long!");
		}
		newProductDto.setProductDescription(productDTO.getProductDescription());

		if (!productValidator.isProductDataSizeCorrect(productDTO.getProductCategory(), 3, 30)) {
			throw new InvalidInputDataException("Product Category has to be between 3 and 30 characters long!");
		}
		newProductDto.setProductCategory(productDTO.getProductCategory());

		newProductDto.setProductStock(productDTO.getProductStock());

		productRepository.save(ProductMapper.updateDtoToProductEntity(product, newProductDto));

		return ProductMapper.ProductEntityToDto(product);
	}
}
