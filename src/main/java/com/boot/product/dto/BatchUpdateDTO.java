package com.boot.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BatchUpdateDTO {
    @NotNull
    private String productName;
    @Positive(message = "Product quantity should be positive number!")
    private int quantity;
    @NotNull
    private Operation operation;
}
