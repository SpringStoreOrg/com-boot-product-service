package com.boot.product.controller;

import java.util.List;

import com.boot.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.service.ProductService;


import javax.validation.Valid;
import javax.validation.constraints.Size;

@Controller
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product) throws InvalidInputDataException {
        ProductDTO newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/delete/{productName}")
    public ResponseEntity<ProductDTO> deleteProductByProductName(@Size(min = 2, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName)
            throws EntityNotFoundException {
        ProductDTO product =  productService.deleteProductByProductName(productName);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> findAllProducts(@RequestParam String products) throws EntityNotFoundException {
        List<ProductDTO> productList = productService.findAllProducts(products);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/{productName}")
    @ResponseBody
    public ResponseEntity<ProductDTO> findProductByProductName(@Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName,  @RequestParam(value="includeInactive", defaultValue = "false")
            Boolean includeInactive)
            throws EntityNotFoundException {
        ProductDTO product;
        if (includeInactive) {
            product = productService.getProductByProductName(productName);

        } else {
            product = productService.getProductByProductNameInactive(productName);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductDTO> updateProductByProductName(@Valid @RequestBody ProductDTO product,
                                                                 @Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName) throws InvalidInputDataException {
        ProductDTO productDTO = productService.updateProductByProductName(productName, product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
