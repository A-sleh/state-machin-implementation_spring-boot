package com.thehecklers.track_order.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thehecklers.track_order.OrderState;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="order_transaction_history")
public class OrderLogHistory {
    @Id @GeneratedValue
    public Long id;
    @JsonProperty("transaction_date")
    public Date transactionDate;
    @JsonProperty("order_id")
    public Long orderId;

    @JsonProperty("is_success")
    public boolean isSuccess;

    @Enumerated(EnumType.STRING)
    @JsonProperty("last_order_state")
    public OrderState lastOrderState;

    @Enumerated(EnumType.STRING)
    @JsonProperty("new_order_state")
    public OrderState newOrderState;
}
