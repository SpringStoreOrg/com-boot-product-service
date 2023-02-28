package com.boot.product.service;

import com.boot.command.InsufficientProductsInStock;
import com.boot.command.ProductsReserved;
import com.boot.command.ReserveProductsCommand;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

@Slf4j
@Service
@AllArgsConstructor
public class ProductCommandHandler {
    private ProductService productService;
    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("productService")
                .onMessage(ReserveProductsCommand.class, this::reserveProducts)
                .build();
    }

    public Message reserveProducts(CommandMessage<ReserveProductsCommand> cm) {
        ReserveProductsCommand cmd = cm.getCommand();
        try {
            productService.reserveProducts(cmd.getOrderDTO());
            return withSuccess(new ProductsReserved());
        } catch (Exception e) {
            log.error(e.getMessage());
            return withFailure(new InsufficientProductsInStock());
        }
    }
}
