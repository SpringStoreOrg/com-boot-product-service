package com.boot.product.controller;

import com.boot.product.dto.StockDTO;
import com.boot.product.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@Controller
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;

    @PostMapping("/reserve")
    public ResponseEntity<List<StockDTO>> reserveProducts(@Valid @RequestBody @Size(min = 1) List<StockDTO> stockList){
        List<StockDTO> result = stockService.reserveProducts(stockList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/release")
    public ResponseEntity releaseProducts(@Valid @RequestBody @Size(min = 1) List<StockDTO> stockList){
        stockService.releaseProducts(stockList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
