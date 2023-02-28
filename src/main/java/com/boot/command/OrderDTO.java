package com.boot.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Size(min = 1, message = "Order should have at least one entry")
    private List<OrderEntryDTO> entryList;
}
