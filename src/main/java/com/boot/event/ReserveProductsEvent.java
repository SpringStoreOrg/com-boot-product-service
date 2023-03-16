package com.boot.event;

import com.boot.command.OrderEntryDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ReserveProductsEvent {
    private UUID orderId;
    private String email;
    private long userId;
    private List<OrderEntryDTO> entries;
}
