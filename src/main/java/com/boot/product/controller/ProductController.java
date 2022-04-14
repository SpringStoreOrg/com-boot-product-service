package com.boot.product.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.service.ProductService;
import com.boot.services.dto.ProductDTO;

@Controller
@RequestMapping("/")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/addProduct")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product) throws InvalidInputDataException {
		ProductDTO newProduct = productService.addProduct(product);
		return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
	}

	@DeleteMapping("/deleteProductByProductName/{productName}")
	public ResponseEntity<ProductDTO> deleteProductByProductName(@PathVariable("productName") String productName)
			throws EntityNotFoundException {
		productService.deleteProductByProductName(productName);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<List<ProductDTO>> findAllProducts() throws EntityNotFoundException {
		List<ProductDTO> productList = productService.findAllProducts();
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	@GetMapping("/getByProductCategory/{productCategory}")
	@ResponseBody
	public ResponseEntity<List<ProductDTO>> findByProductCategory(@PathVariable("productCategory") String productCategory) {
		List<ProductDTO> productList = productService.findByProductCategory(productCategory);
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	@GetMapping("/getProductById")
	@ResponseBody
	public ResponseEntity<ProductDTO> findProductById(@RequestParam long id) throws EntityNotFoundException {
		ProductDTO product = productService.getProductById(id);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@GetMapping("/getProductByProductName")
	@ResponseBody
	public ResponseEntity<ProductDTO> findProductByProductName(@RequestParam String productName)
			throws EntityNotFoundException {
		ProductDTO product = productService.getProductByProductName(productName);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PutMapping("/updateProductByProductName/{productName}")
	public ResponseEntity<ProductDTO> updateProductByProductName(@RequestBody ProductDTO product,
			@PathVariable("productName") String productName) throws EntityNotFoundException, InvalidInputDataException {
		ProductDTO productDTO = productService.updateProductByProductName(productName, product);
		return new ResponseEntity<>(productDTO, HttpStatus.OK);
	}
}
