package com.boot.product.controller;

import com.boot.product.dto.PagedResponseDTO;
import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.service.ProductService;
import com.boot.product.util.ProductUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
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
    public ResponseEntity<PagedResponseDTO> getProducts(@RequestParam(required = false) String productNames,
                                                        @RequestParam(value = "includeInactive", defaultValue = "false") Boolean includeInactive,
                                                        @Size(min = 3, max = 30, message = "Product Category size has to be between 2 and 30 characters!") @RequestParam(value = "category", defaultValue = "") String category,
                                                        @PageableDefault(size = 10, direction = Sort.Direction.ASC, sort = {"name"}) Pageable pageable) {
        PagedResponseDTO pagedResponse = new PagedResponseDTO();
        pagedResponse.setCurrentPage(pageable.getPageNumber());

        if (StringUtils.isNotBlank(productNames)) {
            pagedResponse.setProducts(productService.findAllProducts(productNames, includeInactive, pageable));
            pagedResponse.setTotalItems(productService.getAllProductsCount(productNames, includeInactive));
        } else {
            if (category.isEmpty()) {
                pagedResponse.setProducts(productService.getAllProducts(pageable));
                pagedResponse.setTotalItems(productService.getAllProductsCount());
            } else {
                pagedResponse.setProducts(productService.findByCategoryAndStatus(category, pageable));
                pagedResponse.setTotalItems(productService.getByCategoryAndStatusCount(category));
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
    public ResponseEntity<ProductDTO> findProductBySlug(@Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("slug") String productName, @RequestParam(required = false, value = "includeInactive", defaultValue = "false")
            Boolean includeInactive)
            throws EntityNotFoundException {

        ProductDTO product = productService.getProductBySlug(ProductUtil.getSlugIfName(productName), includeInactive);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{productName}/info")
    @ResponseBody
    public ProductInfoDTO getProductInfoByProductName(@PathVariable("productName") String productName)
            throws EntityNotFoundException {
        return productService.getProductInfoByName(productName);
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductDTO> updateProductByProductName(@Valid @RequestBody ProductDTO product,
                                                                 @Size(min = 3, max = 30, message = "Product Name size has to be between 2 and 30 characters!") @PathVariable("productName") String productName) throws InvalidInputDataException {
        ProductDTO productDTO = productService.updateProductByProductName(productName, product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    private int getPagesCount(int totalItems, int pageSize) {
        return (totalItems / pageSize) + (totalItems % pageSize > 0 ? 1 : 0);
    }
}
