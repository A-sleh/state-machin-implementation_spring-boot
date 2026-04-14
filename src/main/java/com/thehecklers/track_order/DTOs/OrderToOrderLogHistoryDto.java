package com.thehecklers.track_order.dtos;

import com.thehecklers.track_order.OrderState;
import com.thehecklers.track_order.OrderTrigger;
import com.thehecklers.track_order.entities.Order;
import com.thehecklers.track_order.entities.OrderLogHistory;

import java.util.Date;

public class OrderToOrderLogHistoryDto {
    private final Long orderId;
    private final Date transactionDate;
    private final boolean success;
    private final OrderState lastOrderState;
    private final OrderState newOrderState;
    private final OrderTrigger trigger;

    public OrderToOrderLogHistoryDto(Long orderId,
                                     Date transactionDate,
                                     boolean success,
                                     OrderState lastOrderState,
                                     OrderState newOrderState,
                                     OrderTrigger trigger) {
        this.orderId = orderId;
        this.transactionDate = transactionDate;
        this.success = success;
        this.lastOrderState = lastOrderState;
        this.newOrderState = newOrderState;
        this.trigger = trigger;
    }

    public static OrderToOrderLogHistoryDto from(Order order,
                                                 OrderTrigger trigger,
                                                 OrderState lastOrderState,
                                                 OrderState newOrderState,
                                                 boolean success) {
        return new OrderToOrderLogHistoryDto(
                order.id,
                new Date(),
                success,
                lastOrderState,
                newOrderState,
                trigger
        );
    }

    public OrderLogHistory toEntity() {
        OrderLogHistory history = new OrderLogHistory();
        history.transactionDate = transactionDate;
        history.orderId = orderId;
        history.isSuccess = success;
        history.lastOrderState = lastOrderState;
        history.newOrderState = newOrderState;
        return history;
    }
}
