package com.boot.product.service;

import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.model.Product;
import com.boot.product.repository.ProductRepository;
import com.boot.product.validator.ProductValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.boot.product.model.Product.*;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private ProductValidator productValidator;

    private ProductRepository productRepository;

    public ProductDTO addProduct(ProductDTO productDTO) {
        log.info("addProduct - process started");

        if (productValidator.isNamePresent(productDTO.getName())) {
            throw new InvalidInputDataException("The Selected Product name is already used!");
        }
        productDTO.setStatus(ProductStatus.ACTIVE);

        Product product = productRepository.save(dtoToProductEntity(productDTO));

        return productEntityToDto(product);
    }

    public ProductDTO deleteProductByProductName(String productName) {
        if (!productValidator.isNamePresent(productName)) {
            throw new EntityNotFoundException(
                    "Product with Product Name: " + productName + " not found in the Database!");
        }
        Product product = productRepository.findByNameAndStatus(productName, ProductStatus.ACTIVE);
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);

        log.info("{} - successfully set as INACTIVE", productName);
        return productEntityToDto(product);
    }

    public List<ProductDTO> findAllProducts(@NotNull String productParam, Boolean includeInactive, Pageable pageable) {

        log.info("findAllProducts - process started");

        List<String> prodList = Stream.of(productParam.split(",", -1)).collect(Collectors.toList());

        List<Product> productList;

        if (includeInactive) {
            productList = productRepository.findByNameIn(prodList, pageable);

        } else {
            productList = productRepository.findByNameInAndStatus(prodList, ProductStatus.ACTIVE, pageable);
        }

        return productEntityToDtoList(productList);
    }

    public int getAllProductsCount(@NotNull String productParam, Boolean includeInactive) {

        List<String> prodList = Stream.of(productParam.split(",", -1)).collect(Collectors.toList());
        if (includeInactive) {
            return productRepository.countAllByNameIn(prodList);
        } else {
            return productRepository.countAllByNameInAndStatus(prodList, ProductStatus.ACTIVE);
        }
    }

    public List<ProductDTO> getAllProducts(Pageable pageable) {

        log.info("getAllProducts - process started");

        List<Product> prodList = productRepository.findByStatus(ProductStatus.ACTIVE, pageable);

        return productEntityToDtoList(prodList);
    }

    public int getAllProductsCount() {
        return productRepository.countAllByStatus(ProductStatus.ACTIVE);
    }

    public ProductDTO getProductBySlug(String slug, Boolean includeInactive) {

        log.info("getProductByProductName - process started");
        Product product;
        if (includeInactive) {
            product = productRepository.findBySlug(slug);
        } else {
            product = productRepository.findBySlugAndStatus(slug, ProductStatus.ACTIVE);
        }

        if (product == null) {
            throw new EntityNotFoundException("Could not find any product in the database");
        }

        return productEntityToDto(product);
    }

    public List<String> getProductsByPartialName(String name, Pageable pageable) {

        log.info("getProductsByPartialText - process started");
        return productRepository.findByNameContainingIgnoreCase("%"+name.toLowerCase()+"%", pageable);
    }

    public ProductDTO updateProductByProductName(String productName, ProductDTO productDTO)
            throws InvalidInputDataException {

        Product product = productRepository.findByName(productName);

        ProductDTO newProductDto = productEntityToDto(product);

        if (product.getName().matches(productName)) {
            newProductDto.setName(productDTO.getName());
        } else if (productValidator.isNamePresent(productDTO.getName())) {
            throw new InvalidInputDataException("The Selected Product name is already used!");
        }
        newProductDto.setImages(productDTO.getImages());

        newProductDto.setPrice(productDTO.getPrice());

        newProductDto.setCategory(productDTO.getCategory());

        newProductDto.setStock(productDTO.getStock());

        productRepository.save(updateDtoToProductEntity(product, newProductDto));

        return productEntityToDto(product);
    }

    public List<ProductDTO> findByCategoryAndStatus(String category, Pageable pageable) {

        log.info("findByCategoryAndStatus - process started");

        List<Product> productList = productRepository.findByCategoryAndStatus(category, ProductStatus.ACTIVE, pageable);

        ArrayList<ProductDTO> productDTOList = new ArrayList<>();

        productList.forEach(p -> productDTOList.add(productEntityToDto(p)));

        return productDTOList;
    }

    public int getByCategoryAndStatusCount(String category) {
        return productRepository.countAllByCategoryAndStatus(category, ProductStatus.ACTIVE);
    }

    public ProductInfoDTO getProductInfoByName(String productName) {
        return productRepository.getActiveProductInfo(productName);
    }

    public List<ProductInfoDTO> getProductsInfo(String productNames) {
        return productRepository.getActiveProductsInfo(
                Arrays.stream(productNames.split(","))
                        .collect(Collectors.toList()));
    }
}
