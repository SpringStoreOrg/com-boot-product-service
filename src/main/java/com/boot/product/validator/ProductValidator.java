
package com.boot.product.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.product.repository.ProductRepository;

@Service
public class ProductValidator {

	@Autowired
	private ProductRepository productRepository;

	public boolean isProductNamePresent(String productName) {

		if (productRepository.findByProductName(productName) == null)
			return false;
		return true;
	}

	public boolean isIdPresent(long id) {
		if (productRepository.getProductById(id) == null)
			return false;
		return true;
	}

	public boolean isProductDataSizeCorrect(String userData, int min, int max) {
		if (userData == null ||userData.length() < min || userData.length() > max)
			return false;
		return true;
	}
}
