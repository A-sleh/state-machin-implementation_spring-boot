package com.thehecklers.track_order.controlers;

import com.thehecklers.track_order.OrderTrigger;
import com.thehecklers.track_order.entities.Order;
import com.thehecklers.track_order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> getAllOrders() {
        return this.orderService.getAllOrders();
    }

    @PostMapping
    public Order newOrder(@RequestBody Order order) {
        return this.orderService.addOrder(order);
    }

    @PatchMapping("/{id}/trigger")
    public Order triggerOrder(@PathVariable Long id, @RequestParam OrderTrigger trigger) {
        return this.orderService.fireTrigger(id, trigger);
    }
}
