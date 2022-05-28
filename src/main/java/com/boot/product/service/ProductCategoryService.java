package com.boot.product.service;

import com.boot.product.model.Product;
import com.boot.product.repository.ProductCategoryRepository;
import com.boot.product.repository.ProductRepository;
import com.boot.product.dto.ProductDTO;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.boot.product.model.Product.productEntityToDto;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductCategoryService {

	private ProductCategoryRepository productCategoryRepository;

	public List<ProductDTO> findByProductCategory(String productCategory) {

		List<Product> productList = productCategoryRepository.findByCategory(productCategory);

		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		productList.stream().forEach(p -> productDTOList.add(productEntityToDto(p)));

		return productDTOList;
	}
}
