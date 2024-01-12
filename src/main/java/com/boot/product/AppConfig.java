package com.boot.product;

import com.boot.product.dto.CreateProductDTO;
import com.boot.product.dto.ProductDTO;
import com.boot.product.dto.ProductDetailsDTO;
import com.boot.product.model.Image;
import com.boot.product.model.ImageType;
import com.boot.product.model.Product;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {
    @Value("${product.service.url}")
    private String productServiceUrl;
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(CreateProductDTO.class, Product.class);
        Converter<List<Image>, List<String>> imagesToList = list -> list.getSource().stream()
                .filter(item -> ImageType.FULL.equals(item.getType()))
                .map(image -> getImageFromUrl(image))
                .collect(Collectors.toList());
        Converter<List<Image>, String> imagesToThumbnail = list -> list.getSource().stream()
                .filter(item -> ImageType.THUMBNAIL.equals(item.getType()))
                .map(image -> getImageFromUrl(image))
                .findFirst().orElse(
                        list.getSource().stream()
                                .map(image -> getImageFromUrl(image))
                                .findFirst()
                                .orElse(null));
        modelMapper.typeMap(Product.class, ProductDetailsDTO.class)
                .addMappings(
                        mapper -> mapper.using(imagesToList).map(Product::getImages, ProductDetailsDTO::setImages)
                )
                .addMappings(
                        mapper -> mapper.using(imagesToThumbnail).map(Product::getImages, ProductDetailsDTO::setThumbnail)
                );
        modelMapper.typeMap(Product.class, ProductDTO.class)
                .addMappings(
                        mapper -> mapper.using(imagesToThumbnail).map(Product::getImages, ProductDTO::setThumbnail)
                );

        return modelMapper;
    }

    private String getImageFromUrl(Image image){
        if(image.getName().startsWith("https://")){
            return image.getName();
        }
        return String.format("%s/%s/image/%s", productServiceUrl, image.getProduct().getSlug(), image.getName());
    }
}
