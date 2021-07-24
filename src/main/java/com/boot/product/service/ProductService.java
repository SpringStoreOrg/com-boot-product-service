package com.boot.product.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

@Slf4j
@Service
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

	public void deleteProductById(long id) throws EntityNotFoundException {
		if (!productValidator.isIdPresent(id)) {
			throw new EntityNotFoundException("Product with Id : " + id + " not found in the Database!");
		}
		productRepository.deleteById(id);
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
						userServiceClient.callUpdateUser(user.getUserName(), user);
						log.info(
								productName + " - succesfully deleted from User with User Name: " + user.getUserName());
					}
				}
			}
		}

		productRepository.deleteByProductName(productName);
		log.info(productName + " - succesfully deleted from the Database");

	}

	public List<ProductDTO> findAllProducts() throws EntityNotFoundException {

		List<Product> productList = productRepository.findAll();

		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		productList.stream().forEach(p -> productDTOList.add(ProductMapper.ProductEntityToDto(p)));

		return productDTOList;
	}

	public List<ProductDTO> findByProductCategory(String productCategory) {

		List<Product> productList = productRepository.findByProductCategory(productCategory);

		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		productList.stream().forEach(p -> productDTOList.add(ProductMapper.ProductEntityToDto(p)));

		return productDTOList;
	}

	public ProductDTO getProductById(long id) throws EntityNotFoundException {

		if (!productValidator.isIdPresent(id)) {
			throw new EntityNotFoundException("Could not find any product in the database");
		}
		Product product = productRepository.getProductById(id);

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

	public Set<String> findAllProductCategories() throws EntityNotFoundException {
		List<Product> products = productRepository.findAll();

		Set<String> categories = new HashSet<>();

		products.stream().forEach(p -> categories.add(p.getProductCategory()));

		if (categories.isEmpty()) {
			throw new EntityNotFoundException("Could not find any product categories in the database");
		}
		return categories;
	}

	public ProductDTO updateProductByProductName(String productName, ProductDTO productDTO)
			throws InvalidInputDataException {

		Product product = productRepository.findByProductName(productName);

		ProductDTO newProductDto = ProductMapper.ProductEntityToDto(product);

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
