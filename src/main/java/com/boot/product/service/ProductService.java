package com.boot.product.service;

import com.boot.product.dto.CreateProductDTO;
import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductDetailsDTO;
import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.enums.ProductStatus;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.model.Image;
import com.boot.product.model.ImageType;
import com.boot.product.model.Product;
import com.boot.product.repository.CharacteristicsRepository;
import com.boot.product.repository.ProductRepository;
import com.boot.product.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductValidator productValidator;
    private final ProductRepository productRepository;
    private final CharacteristicsRepository characteristicsRepository;
    private final ModelMapper modelMapper;

    @Value("${image.directory}")
    private String imageDirectory;
    @Value("${thumbnail.image.size}")
    private int thumbnailSize;

    public ProductDetailsDTO addProduct(CreateProductDTO productDTO) {
        log.info("addProduct - process started");

        if (productValidator.isNamePresent(productDTO.getName())) {
            throw new InvalidInputDataException("The Selected Product name is already used!");
        }
        productDTO.setStatus(ProductStatus.ACTIVE);
        Product product = modelMapper.map(productDTO, Product.class);
        if (StringUtils.isNotEmpty(productDTO.getThumbnail())) {
            try {
                String thumbnailImage = generateThumbnail(new File(imageDirectory, product.getSlug()), productDTO.getThumbnail());
                if (StringUtils.isNotEmpty(thumbnailImage)) {
                    Image image = new Image();
                    image.setType(ImageType.THUMBNAIL);
                    image.setName(thumbnailImage);
                    product.getImages().add(image);
                }
            } catch (IOException e) {
            }
        }
        if (CollectionUtils.isNotEmpty(product.getImages())) {
            product.getImages().forEach(image -> image.setProduct(product));
        }
        Product persistedProduct = productRepository.save(product);

        product.getCharacteristics().setProduct(persistedProduct);
        characteristicsRepository.save(persistedProduct.getCharacteristics());

        return modelMapper.map(persistedProduct, ProductDetailsDTO.class);
    }

    public ProductDetailsDTO deleteProductByProductName(String productName) {
        if (!productValidator.isNamePresent(productName)) {
            throw new EntityNotFoundException(
                    "Product with Product Name: " + productName + " not found in the Database!");
        }
        Product product = productRepository.findByNameAndStatus(productName, ProductStatus.ACTIVE);
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);

        log.info("{} - successfully set as INACTIVE", productName);
        return modelMapper.map(product, ProductDetailsDTO.class);
    }

    public List<ProductDTO> findAllProducts(@NotNull String productParam, Boolean includeInactive, Pageable pageable) {

        log.info("findAllProducts - process started");

        List<String> prodList = Stream.of(productParam.split(",", -1)).collect(Collectors.toList());

        List<Product> productList;

        if (includeInactive) {
            productList = productRepository.findBySlugIn(prodList, pageable);

        } else {
            productList = productRepository.findBySlugInAndStatus(prodList, ProductStatus.ACTIVE, pageable);
        }

        return productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .collect(Collectors.toList());
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

        return prodList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public int getAllProductsCount() {
        return productRepository.countAllByStatus(ProductStatus.ACTIVE);
    }

    public ProductDetailsDTO getProductBySlug(String slug, Boolean includeInactive) {

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

        return modelMapper.map(product, ProductDetailsDTO.class);
    }

    public List<String> getProductsByPartialName(String name, Pageable pageable) {

        log.info("getProductsByPartialText - process started");
        return productRepository.findByNameContainingIgnoreCase("%"+name.toLowerCase()+"%", pageable);
    }

    public ProductDetailsDTO updateProductByProductName(String productName, CreateProductDTO productDTO)
            throws InvalidInputDataException {

        Product product = productRepository.findByName(productName);

        if (product.getName().matches(productName)) {
            product.setName(productDTO.getName());
        } else if (productValidator.isNamePresent(productDTO.getName())) {
            throw new InvalidInputDataException("The Selected Product name is already used!");
        }
        //product.setImages(productDTO.getImages());

        product.setPrice(productDTO.getPrice());

        product.setCategory(productDTO.getCategory());

        product.setStock(productDTO.getStock());

        productRepository.save(product);

        return modelMapper.map(product, ProductDetailsDTO.class);
    }

    public List<ProductDTO> findByCategoryAndStatus(String category, Pageable pageable) {

        log.info("findByCategoryAndStatus - process started");

        List<Product> productList = productRepository.findByCategoryAndStatus(category, ProductStatus.ACTIVE, pageable);

        return productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findByPartialNameAndStatus(String partialName, Pageable pageable) {

        log.info("findByPartialNameAndStatus - process started");

        List<Product> productList = productRepository.findByPartialNameAndStatus("%" + partialName.toLowerCase() + "%", ProductStatus.ACTIVE, pageable);

        return productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public int getByCategoryAndStatusCount(String category) {
        return productRepository.countAllByCategoryAndStatus(category, ProductStatus.ACTIVE);
    }

    public int getByPartialNameAndStatusCount(String partialName) {
        return productRepository.countByPartialNameAndStatus("%" + partialName.toLowerCase() + "%", ProductStatus.ACTIVE);
    }

    public ProductInfoDTO getProductInfoBySlug(String productSlug) {
        return productRepository.getActiveProductInfo(productSlug);
    }

    public List<ProductInfoDTO> getProductsInfo(String productSlugs) {
        return productRepository.getActiveProductsInfo(
                Arrays.stream(productSlugs.split(","))
                        .collect(Collectors.toList()));
    }

    public boolean isAvailable(String productSlug){
        return !productRepository.existsBySlug(productSlug);
    }

    private String generateThumbnail(File productImages, String originalImageName) throws IOException {
        File originalImage = new File(productImages, originalImageName);
        if(originalImage.exists()){
            String finalFileName = String.format("%s_thumb.%s", FilenameUtils.getBaseName(originalImageName), FilenameUtils.getExtension(originalImageName));
            BufferedImage originalImageBuffered = ImageIO.read(originalImage);
            BufferedImage thumbnail = Scalr.resize(originalImageBuffered, thumbnailSize);
            ImageIO.write(thumbnail, FilenameUtils.getExtension(originalImageName), new File(productImages, finalFileName));

            return finalFileName;
        }

        return StringUtils.EMPTY;
    }
}
