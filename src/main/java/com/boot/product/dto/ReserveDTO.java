package com.boot.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Positive;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
    @Positive(message = "Product quantity should be positive number!")
    private int quantity;
}
