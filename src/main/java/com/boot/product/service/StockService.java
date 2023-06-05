package com.boot.product.service;

import com.boot.product.dto.ProductInfoDTO;
import com.boot.product.dto.StockDTO;
import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.exception.InvalidInputDataException;
import com.boot.product.model.Product;
import com.boot.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StockService {
    private ProductRepository productRepository;

    @Transactional
    public void subtractProducts(List<StockDTO> stockList) {
        log.info("Subtracting products:{}", stockList.stream()
                .map(item->"name:"+ item.getProductName()+" quantity:"+item.getQuantity())
                .collect(Collectors.joining(";")));
        Set<String> productNames = stockList.stream().map(StockDTO::getProductName).collect(Collectors.toSet());
        Map<String, ProductInfoDTO> productInfos = productRepository.getActiveProductsInfo(new ArrayList<>(productNames))
                .stream().collect(Collectors.toMap(ProductInfoDTO::getName, item -> item));
        //check for products existence
        if (productNames.size() > productInfos.keySet().size()) {
            productNames.removeAll(productInfos.keySet());
            log.warn(StringUtils.join(productNames, ",") + " where not found");
            throw new EntityNotFoundException(StringUtils.join(productNames, ",") + " where not found");
        }

        //check for stocks availability
        List<String> quantityFails = new ArrayList<>();
        stockList.forEach(item -> {
            ProductInfoDTO productInfo = productInfos.get(item.getProductName());
            if (item.getQuantity() > productInfo.getQuantity()) {
                log.warn("{} has only {} in stock", item.getProductName(), productInfo.getQuantity());
                quantityFails.add(String.format("{} has only {} in stock", item.getProductName(), productInfo.getQuantity()));
            }
        });
        if (quantityFails.size() > 0) {
            throw new InvalidInputDataException(StringUtils.join(quantityFails, ";"));
        }

        //persist stock changes
        stockList.forEach(item -> {
            Product product = productRepository.findByName(item.getProductName());
            product.subtractItems(item.getQuantity());
            productRepository.save(product);
        });
        log.info("Subtracting products finished:{}", stockList.stream()
                .map(item->"name:"+ item.getProductName()+" quantity:"+item.getQuantity())
                .collect(Collectors.joining(";")));
    }

    @Transactional
    public void addProducts(List<StockDTO> stockList) {
        log.info("Adding products:{}", stockList.stream()
                .map(item->"name:"+ item.getProductName()+" quantity:"+item.getQuantity())
                .collect(Collectors.joining(";")));
        Set<String> productNames = stockList.stream().map(StockDTO::getProductName).collect(Collectors.toSet());
        Map<String, ProductInfoDTO> productInfos = productRepository.getProductsInfo(new ArrayList<>(productNames))
                .stream().collect(Collectors.toMap(ProductInfoDTO::getName, item -> item));
        //check for products existence
        if (productNames.size() > productInfos.keySet().size()) {
            productNames.removeAll(productInfos.keySet());
            throw new EntityNotFoundException(StringUtils.join(productNames, ",") + " where not found");
        }

        //persist stock changes
        stockList.forEach(item -> {
            Product product = productRepository.findByName(item.getProductName());
            product.addItems(item.getQuantity());
            productRepository.save(product);
        });
        log.info("Adding products finished:{}", stockList.stream()
                .map(item->"name:"+ item.getProductName()+" quantity:"+item.getQuantity())
                .collect(Collectors.joining(";")));
    }
}
