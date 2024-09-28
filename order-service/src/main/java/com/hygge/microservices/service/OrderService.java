package com.hygge.microservices.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hygge.microservices.client.InventoryClient;
import com.hygge.microservices.dto.OrderRequest;
import com.hygge.microservices.event.OrderPlacedEvent;
import com.hygge.microservices.model.Order;
import com.hygge.microservices.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

  public void placeOrder(OrderRequest orderRequest) {

    var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

    if (isProductInStock) {

      Order order = new Order();
      order.setOrderNumber(UUID.randomUUID().toString());
      order.setSkuCode(orderRequest.skuCode());
      order.setPrice(orderRequest.price());
      order.setQuantity(orderRequest.quantity());
      orderRepository.save(order);

      OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(),
          orderRequest.userDetails().email());
      log.info("Start - sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
      kafkaTemplate.send("order-placed", orderPlacedEvent);
      log.info("End - sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);

    } else {
      throw new RuntimeException("Product with skuCode" + orderRequest.skuCode() + "is not in stock");
    }
  }

}
