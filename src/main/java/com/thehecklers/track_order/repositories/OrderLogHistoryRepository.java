package com.thehecklers.track_order.repositories;

import com.thehecklers.track_order.entities.OrderLogHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLogHistoryRepository extends CrudRepository<OrderLogHistory, Long> {
}
