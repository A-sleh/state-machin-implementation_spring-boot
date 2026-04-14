package com.thehecklers.track_order;


import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

public class OrderStateMachine {

    private static final StateMachineConfig<OrderState, OrderTrigger> config = new StateMachineConfig<>();

    static {
        config.configure(OrderState.NEW)
                .permit(OrderTrigger.CONFIRM, OrderState.CONFIRMED)
                .permit(OrderTrigger.CANCEL, OrderState.CANCELLED);

        config.configure(OrderState.CONFIRMED)
                .permit(OrderTrigger.SHIP, OrderState.SHIPPED)
                .permit(OrderTrigger.CANCEL, OrderState.CANCELLED);

        config.configure(OrderState.SHIPPED)
                .permit(OrderTrigger.DELIVER, OrderState.DELIVERED);

        config.configure(OrderState.DELIVERED);
        config.configure(OrderState.CANCELLED);
    }

    public static OrderState fire(OrderState currentState, OrderTrigger trigger) {
        StateMachine<OrderState, OrderTrigger> machine = new StateMachine<>(currentState, config);
        machine.fire(trigger);
        return machine.getState();
    }
}
