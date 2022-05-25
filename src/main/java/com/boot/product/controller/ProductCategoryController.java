package com.boot.product.controller;

import com.boot.product.dto.ProductDTO;
import com.boot.product.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@Controller
@RequestMapping("/productCategory")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/{productCategory}")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> findByProductCategory(@Size(min = 3, max = 30, message = "Product Category size has to be between 2 and 30 characters!") @PathVariable("productCategory") String productCategory) {
        List<ProductDTO> productList = productCategoryService.findByProductCategory(productCategory);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
}
