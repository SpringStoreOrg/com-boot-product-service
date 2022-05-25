
package com.boot.product.validator;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.product.repository.ProductRepository;

@Service
@AllArgsConstructor
public class ProductValidator {

    private ProductRepository productRepository;

    public boolean isProductNamePresent(String productName) {

        return productRepository.findByProductName(productName) != null;
    }

}
