package com.boot.product.controller;

import java.util.List;

import com.boot.product.dto.BatchUpdateDTO;
import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductPriceDTO;
import com.boot.product.dto.ReserveDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{productName}")
    public ResponseEntity<ProductDTO> deleteProductByProductName(@Size(min = 2, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName)
            throws EntityNotFoundException {
        ProductDTO product = productService.deleteProductByProductName(productName);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) String productNames, @RequestParam(value = "includeInactive", defaultValue = "false")
            Boolean includeInactive, @Size(min = 3, max = 30, message = "Product Category size has to be between 2 and 30 characters!") @RequestParam(value = "category", defaultValue = "")
                                                               String category) {
        List<ProductDTO> productList;

        if (StringUtils.isNotBlank(productNames)) {
            productList = productService.findAllProducts(productNames, includeInactive);
        } else {
            if (category.isEmpty()) {
                productList = productService.getAllProducts();
            } else {
                productList = productService.findByCategoryAndStatus(category);
            }
        }

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/prices")
    @ResponseBody
    public List<ProductPriceDTO> getProductPrices() {
        return productService.getProductPrices();
    }

    @GetMapping("/{productName}")
    @ResponseBody
    public ResponseEntity<ProductDTO> findProductByProductName(@Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName, @RequestParam(required = false, value = "includeInactive", defaultValue = "false")
            Boolean includeInactive)
            throws EntityNotFoundException {

        ProductDTO product = productService.getProductByProductName(productName, includeInactive);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductDTO> updateProductByProductName(@Valid @RequestBody ProductDTO product,
                                                                 @Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName) throws InvalidInputDataException {
        ProductDTO productDTO = productService.updateProductByProductName(productName, product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/{productName}/reserve")
    public ResponseEntity reserve(@PathVariable("productName") String productName, @Valid @RequestBody ReserveDTO reserveDTO){
        productService.reserve(productName, reserveDTO.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productName}/reserve/release")
    public ResponseEntity reserveRelease(@PathVariable("productName") String productName, @Valid @RequestBody ReserveDTO stockDTO){
        productService.reserveRelease(productName, stockDTO.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/batch/reserve")
    @Validated
    public ResponseEntity batchReserve(@Valid @RequestBody List<BatchUpdateDTO> reserveDTO){
        productService.batchReserve(reserveDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/batch/reserve/release")
    public ResponseEntity batchReserveRelease(@Valid @RequestBody List<BatchUpdateDTO> reserveDTO){
        productService.batchReserveRelease(reserveDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
