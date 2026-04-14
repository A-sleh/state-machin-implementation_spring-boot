package com.thehecklers.track_order.services;

import com.thehecklers.track_order.entities.OrderLogHistory;
import com.thehecklers.track_order.repositories.OrderLogHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLogHistoryService {
    private OrderLogHistoryRepository orderLogHistoryRepository;

    @Autowired
    public OrderLogHistoryService(OrderLogHistoryRepository orderLogHistoryRepository) {
        this.orderLogHistoryRepository = orderLogHistoryRepository;
    }

    public OrderLogHistory saveOrderTransaction(OrderLogHistory orderLogHistory) {
        return this.orderLogHistoryRepository.save(orderLogHistory);
    }
}
