package com.thehecklers.track_order.entities;

import com.thehecklers.track_order.OrderState;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue
    public Long id;
    public Date date;
    public int items;
    public double total_price;

    @Enumerated(EnumType.STRING)
    public OrderState state;
}
