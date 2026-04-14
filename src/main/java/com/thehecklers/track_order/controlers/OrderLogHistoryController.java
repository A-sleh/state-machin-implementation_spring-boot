package com.thehecklers.track_order.controlers;

import com.thehecklers.track_order.entities.OrderLogHistory;
import com.thehecklers.track_order.repositories.OrderLogHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders-history")
public class OrderLogHistoryController {

    @Autowired
    private OrderLogHistoryRepository orderLogHistoryRepository;

    @GetMapping
    public Iterable<OrderLogHistory> getOrdersHistory() {
        return this.orderLogHistoryRepository.findAll();
    }
}
