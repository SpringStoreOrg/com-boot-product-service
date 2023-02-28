package com.boot.command;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntryDTO {
    @NotNull
    private String productName;
    @Positive(message = "Product quantity should be positive number!")
    private int quantity;
}
