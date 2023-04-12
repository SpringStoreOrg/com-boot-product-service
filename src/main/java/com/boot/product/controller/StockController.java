package com.boot.product.controller;

import com.boot.product.dto.StockDTO;
import com.boot.product.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;

    @PostMapping("/subtract")
    public ResponseEntity subtractProducts(@Valid @RequestBody List<StockDTO> stockList){
        stockService.subtractProducts(stockList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addProducts(@Valid @RequestBody List<StockDTO> stockList){
        stockService.addProducts(stockList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
