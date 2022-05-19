package com.boot.product.service;

import com.boot.product.repository.ProductRepository;
import com.boot.services.dto.ProductDTO;
import com.boot.services.mapper.ProductMapper;
import com.boot.services.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional
public class ProductCategoryService {


	@Autowired
	private ProductRepository productRepository;

	public List<ProductDTO> findByProductCategory(String productCategory) {

		List<Product> productList = productRepository.findByProductCategory(productCategory);

		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		productList.stream().forEach(p -> productDTOList.add(ProductMapper.ProductEntityToDto(p)));

		return productDTOList;
	}
}
