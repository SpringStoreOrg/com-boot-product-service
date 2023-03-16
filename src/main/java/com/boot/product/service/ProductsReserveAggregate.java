package com.boot.product.service;

import com.boot.command.ReserveProductsCommand;
import com.boot.event.ReserveProductsEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
@Slf4j
public class ProductsReserveAggregate {
    private UUID orderId;
    private boolean handled;

    public ProductsReserveAggregate() {
    }

    @CommandHandler
    public ProductsReserveAggregate(ReserveProductsCommand reserveProductsCommand) {

        if(!handled){
            this.orderId = reserveProductsCommand.getOrderId();
            ReserveProductsEvent reserveProductsEvent  = ReserveProductsEvent.builder()
                    .orderId(reserveProductsCommand.getOrderId())
                    .email(reserveProductsCommand.getEmail())
                    .userId(reserveProductsCommand.getUserId())
                    .entries(reserveProductsCommand.getEntries())
                    .build();

            AggregateLifecycle.apply(reserveProductsEvent);
            handled = true;
            log.info("{} ReserveProductsCommand handled", reserveProductsCommand.getOrderId());
        }else{
            log.info("{} ReserveProductsCommand already handled", reserveProductsCommand.getOrderId());
        }
    }

    @EventSourcingHandler
    public void on(ReserveProductsEvent event) {
        this.handled = true;
        log.info("{} ReserveProductsEvent handled", event.getOrderId());
    }

    @Override
    @AggregateIdentifier
    public String toString() {
        return orderId + "-product";
    }
}
