package com.thehecklers.track_order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStateMachineTest {

    @Test
    void shouldTransitionFromNewToConfirmed() {
        OrderState nextState = OrderStateMachine.fire(OrderState.NEW, OrderTrigger.CONFIRM);

        assertEquals(OrderState.CONFIRMED, nextState);
    }

    @Test
    void shouldRejectInvalidTransition() {
        assertThrows(IllegalStateException.class, () -> OrderStateMachine.fire(OrderState.NEW, OrderTrigger.SHIP));
    }
}
