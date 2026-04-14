package com.thehecklers.track_order.services;

import com.thehecklers.track_order.OrderState;
import com.thehecklers.track_order.OrderStateMachine;
import com.thehecklers.track_order.OrderTrigger;
import com.thehecklers.track_order.dtos.OrderToOrderLogHistoryDto;
import com.thehecklers.track_order.entities.Order;
import com.thehecklers.track_order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLogHistoryService orderLogHistoryService;

    @Autowired
    public OrderService(OrderLogHistoryService orderLogHistoryService) {
        this.orderLogHistoryService = orderLogHistoryService;
    }

    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return this.orderRepository.findById(id);
    }

    public Order addOrder(Order order) {
        if (order.state == null) {
            order.state = OrderState.NEW;
        }
        return this.orderRepository.save(order);
    }

    public Order fireTrigger(Long orderId, OrderTrigger trigger) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found"));

        OrderState currentState = order.state == null ? OrderState.NEW : order.state;
        order.state = currentState;

        try {
            OrderState nextState = OrderStateMachine.fire(currentState, trigger);
            order.state = nextState;
            Order savedOrder = this.orderRepository.save(order);

            this.orderLogHistoryService.saveOrderTransaction(
                    OrderToOrderLogHistoryDto.from(savedOrder, trigger, currentState, nextState, true)
                            .toEntity()
            );

            return savedOrder;
        } catch (IllegalStateException ex) {
            this.orderLogHistoryService.saveOrderTransaction(
                    OrderToOrderLogHistoryDto.from(order, trigger, currentState, currentState, false)
                            .toEntity()
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
