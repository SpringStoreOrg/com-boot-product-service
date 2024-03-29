package com.boot.product.controller;

import com.boot.product.dto.*;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.service.ProductService;
import com.boot.product.util.ProductUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductDetailsDTO> addProduct(@Valid @RequestBody CreateProductDTO product) throws InvalidInputDataException {
        ProductDetailsDTO newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<ProductDetailsDTO> deleteProductByProductName(@Size(min = 2, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName)
            throws EntityNotFoundException {
        ProductDetailsDTO product = productService.deleteProductByProductName(productName);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<PagedResponseDTO> getProducts(@RequestParam(required = false) String productNames,
                                                        @RequestParam(value = "includeInactive", defaultValue = "false") Boolean includeInactive,
                                                        @Size(min = 3, max = 30, message = "Product Category size has to be between 3 and 30 characters!") @RequestParam(value = "category", defaultValue = "") String category,
                                                        @Size(min = 3, max = 30, message = "Product Name size has to be between 3 and 30 characters!") @RequestParam(value = "partialName", defaultValue = "") String partialName,
                                                        @PageableDefault(size = 10, direction = Sort.Direction.ASC, sort = {"name"}) Pageable pageable) {
        PagedResponseDTO pagedResponse = new PagedResponseDTO();
        pagedResponse.setCurrentPage(pageable.getPageNumber());

        if (StringUtils.isNotBlank(productNames)) {
            pagedResponse.setProducts(productService.findAllProducts(productNames, includeInactive, pageable));
            pagedResponse.setTotalItems(productService.getAllProductsCount(productNames, includeInactive));
        } else {
            if (!category.isEmpty()) {
                pagedResponse.setProducts(productService.findByCategoryAndStatus(category, pageable));
                pagedResponse.setTotalItems(productService.getByCategoryAndStatusCount(category));
            } else if (!partialName.isEmpty()) {
                pagedResponse.setProducts(productService.findByPartialNameAndStatus(partialName, pageable));
                pagedResponse.setTotalItems(productService.getByPartialNameAndStatusCount(partialName));
            } else {
                pagedResponse.setProducts(productService.getAllProducts(pageable));
                pagedResponse.setTotalItems(productService.getAllProductsCount());
            }
        }
        pagedResponse.setTotalPages(getPagesCount(pagedResponse.getTotalItems(), pageable.getPageSize()));

        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }

    @GetMapping("/info")
    @ResponseBody
    public List<ProductInfoDTO> getProductInfo(@RequestParam String productNames) {
        return productService.getProductsInfo(productNames);
    }

    @GetMapping("/{slug}")
    @ResponseBody
    public ResponseEntity<ProductDetailsDTO> findProductBySlug(@Size(min = 3, max = 30, message = "Product Name size has to be between 3 and 30 characters!") @PathVariable("slug") String productName, @RequestParam(required = false, value = "includeInactive", defaultValue = "false")
            Boolean includeInactive)
            throws EntityNotFoundException {

        ProductDetailsDTO product = productService.getProductBySlug(ProductUtil.getSlugIfName(productName), includeInactive);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/partialName")
    @ResponseBody
    public ResponseEntity<List<String>> findProductsByPartialName(@Size(min = 3, max = 30, message = "Product Name size has to be between 3 and 30 characters!") @RequestParam String partialName,
                                                                  @RequestParam("size") int size) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.ASC, "name");

        List<String> products = productService.getProductsByPartialName(partialName, pageable);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productSlug}/info")
    @ResponseBody
    public ProductInfoDTO getProductInfoByProductSlug(@PathVariable("productSlug") String productSlug)
            throws EntityNotFoundException {
        return productService.getProductInfoBySlug(productSlug);
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductDetailsDTO> updateProductByProductName(@Valid @RequestBody CreateProductDTO product,
                                                                       @Size(min = 3, max = 30, message = "Product Name size has to be between 3 and 30 characters!") @PathVariable("productName") String productName) throws InvalidInputDataException {
        ProductDetailsDTO productDTO = productService.updateProductByProductName(productName, product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/{productSlug}/available")
    @ResponseBody
    public ResponseEntity getProductAvailability(@PathVariable("productSlug") String productSlug) {
        if(productService.isAvailable(productSlug)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    private int getPagesCount(int totalItems, int pageSize) {
        return (totalItems / pageSize) + (totalItems % pageSize > 0 ? 1 : 0);
    }
}
