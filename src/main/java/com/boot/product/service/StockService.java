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
    public List<StockDTO> reserveProducts(List<StockDTO> stockList) {
        log.info("Reserving products:{}", stockList.stream()
                .map(item -> String.format(" slug:%s quantity:%s", item.getProductSlug(), item.getQuantity()))
                .collect(Collectors.joining(";")));
        Set<String> productSlugs = stockList.stream().map(StockDTO::getProductSlug).collect(Collectors.toSet());
        Map<String, ProductInfoDTO> productInfos = productRepository.getActiveProductsInfo(new ArrayList<>(productSlugs))
                .stream().collect(Collectors.toMap(ProductInfoDTO::getSlug, item -> item));
        //check for products existence
        if (productSlugs.size() > productInfos.keySet().size()) {
            productSlugs.removeAll(productInfos.keySet());
            log.warn(StringUtils.join(productSlugs, ",") + " where not found");
            throw new EntityNotFoundException(StringUtils.join(productSlugs, ",") + " where not found");
        }

        List<StockDTO> result = new ArrayList<>();
        stockList.forEach(item -> {
            ProductInfoDTO productInfo = productInfos.get(item.getProductSlug());
            int notInStock = 0;
            int newStock = 0;
            if (item.getQuantity() > productInfo.getQuantity()) {
                notInStock = item.getQuantity() - productInfo.getQuantity();
            } else {
                newStock = productInfo.getQuantity() - item.getQuantity();
            }
            productRepository.updateStock(productInfo.getSlug(), newStock);
            result.add(new StockDTO()
                    .setProductSlug(item.getProductSlug())
                    .setQuantity(item.getQuantity())
                    .setNotInStock(notInStock));
        });

        log.info("Reserving products finished:{}", result.stream()
                .map(item -> String.format(" slug:%s quantity:%s", item.getProductSlug(), item.getQuantity()))
                .collect(Collectors.joining(";")));

        return result;
    }

    @Transactional
    public void releaseProducts(List<StockDTO> stockList) {
        log.info("Releasing products:{}", stockList.stream()
                .map(item->String.format(" slug:%s quantity:%s", item.getProductSlug(), item.getQuantity()))
                .collect(Collectors.joining(";")));
        Set<String> productNames = stockList.stream().map(StockDTO::getProductSlug).collect(Collectors.toSet());
        Map<String, ProductInfoDTO> productInfos = productRepository.getProductsInfo(new ArrayList<>(productNames))
                .stream().collect(Collectors.toMap(ProductInfoDTO::getSlug, item -> item));
        //check for products existence
        if (productNames.size() > productInfos.size()) {
            Set<String> slugs = productInfos.values().stream()
                    .map(ProductInfoDTO::getSlug)
                    .collect(Collectors.toSet());
            productNames.removeAll(slugs);
            throw new EntityNotFoundException(StringUtils.join(productNames, ",") + " where not found");
        }

        //persist stock changes
        stockList.forEach(item -> {
            if (productInfos.get(item.getProductSlug()) != null) {
                int currentStock = productInfos.get(item.getProductSlug()).getQuantity();
                productRepository.updateStock(item.getProductSlug(), currentStock + item.getQuantity());
            }
        });
        log.info("Releasing products finished:{}", stockList.stream()
                .map(item->String.format(" slug:%s quantity:%s", item.getProductSlug(), item.getQuantity()))
                .collect(Collectors.joining(";")));
    }
}
