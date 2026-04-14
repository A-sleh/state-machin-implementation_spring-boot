# Aircraft Positions / Order State Machine Demo

This Spring Boot project demonstrates a simple order state machine implementation using `stateless4j` and Spring Data JPA.

## What it is

The application models orders and tracks allowed state transitions through a dedicated state machine. It also stores order history events for each triggered transition.

The main concepts are:

- `Order`: a JPA entity persisted in an H2 in-memory database.
- `OrderState`: an enum representing state values.
- `OrderTrigger`: an enum representing transition events.
- `OrderStateMachine`: a small state machine configuration using `stateless4j`.
- `OrderService`: business logic for creating orders, firing triggers, and logging transitions.
- `OrderController`: REST endpoints for working with orders.

## State machine behavior

The state machine supports these states:

- `NEW`
- `CONFIRMED`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

Allowed transitions:

- `NEW` -> `CONFIRMED` when trigger `CONFIRM` is fired
- `NEW` -> `CANCELLED` when trigger `CANCEL` is fired
- `CONFIRMED` -> `SHIPPED` when trigger `SHIP` is fired
- `CONFIRMED` -> `CANCELLED` when trigger `CANCEL` is fired
- `SHIPPED` -> `DELIVERED` when trigger `DELIVER` is fired

Terminal states: `DELIVERED` and `CANCELLED`.

## How it works

1. The application starts from `ProductionMainApp`.
2. `OrderController` exposes REST endpoints under `/orders`.
3. When a new order is added, `OrderService` sets its state to `NEW` if not already present.
4. When `/orders/{id}/trigger` is called with a `trigger` parameter, `OrderService`:
   - loads the order from the database,
   - computes the next state via `OrderStateMachine.fire(currentState, trigger)`,
   - saves the updated order,
   - logs the transition outcome to `order_transaction_history`.
5. If the trigger is invalid for the current state, the service returns HTTP `400 Bad Request` and still stores a history record marking the failure.

## Project structure

- `src/main/java/com/thehecklers/track_order`
  - `ProductionMainApp.java` — main Spring Boot application class.
  - `OrderState.java` — order states enum.
  - `OrderTrigger.java` — permitted trigger enum.
  - `OrderStateMachine.java` — state machine configuration and firing logic.
- `src/main/java/com/thehecklers/track_order/controlers`
  - `OrderController.java` — REST API for orders.
- `src/main/java/com/thehecklers/track_order/services`
  - `OrderService.java` — order lifecycle and state transition logic.
  - `OrderLogHistoryService.java` — saves transition history.
- `src/main/java/com/thehecklers/track_order/entities`
  - `Order.java` — JPA entity for orders.
  - `OrderLogHistory.java` — JPA entity for transition history.
- `src/main/java/com/thehecklers/track_order/repositories`
  - `OrderRepository.java`
  - `OrderLogHistoryRepository.java`
- `src/main/java/com/thehecklers/track_order/dtos`
  - `OrderToOrderLogHistoryDto.java` — maps order events into history records.

## Configuration

The project uses an in-memory H2 database configured in `src/main/resources/application.properties`.

Key configuration:

- H2 console enabled at `/h2-console`
- JDBC URL: `jdbc:h2:mem:aircraftdb`
- `spring.jpa.hibernate.ddl-auto=update`

There are some OAuth2 GitHub client properties present in `application.properties`, but they are not used by the application logic shown.

## Running the application

Use Maven to run the app:

```bash
./mvnw spring-boot:run
```

Or package it and run the jar:

```bash
./mvnw package
java -jar target/aircraft-positions-0.0.1-SNAPSHOT.jar
```

## REST API examples

Create a new order:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"date":"2026-04-14T00:00:00.000+0000","items":3,"total_price":120.0}'
```

List orders:

```bash
curl http://localhost:8080/orders
```

Fire a trigger on an order:

```bash
curl -X PATCH "http://localhost:8080/orders/1/trigger?trigger=CONFIRM"
```

Valid trigger values: `CONFIRM`, `SHIP`, `DELIVER`, `CANCEL`.

### Error response examples

Invalid trigger for the current state returns `400 Bad Request`:

```bash
curl -X PATCH "http://localhost:8080/orders/1/trigger?trigger=DELIVER"
```

If order `1` is not yet in `SHIPPED` state, this returns a bad request error and logs the failed transition.

Missing order ID returns `404 Not Found`:

```bash
curl -X PATCH "http://localhost:8080/orders/999/trigger?trigger=CONFIRM"
```

If the order does not exist, the service responds with `404 Not Found`.

## Notes

- This is a demo project focused on state transition logic.
- The state machine logic is centralized in `OrderStateMachine` for easier reuse and testing.
- Transition history is persisted separately so you can track both successful and failed trigger attempts.
