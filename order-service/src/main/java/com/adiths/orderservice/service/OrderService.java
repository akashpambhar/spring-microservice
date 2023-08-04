package com.adiths.orderservice.service;

import com.adiths.orderservice.dto.Inventory;
import com.adiths.orderservice.dto.ItemDto;
import com.adiths.orderservice.dto.OrderRequest;
import com.adiths.orderservice.dto.OrderResponse;
import com.adiths.orderservice.model.Item;
import com.adiths.orderservice.model.Order;
import com.adiths.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return new ResponseEntity<>(orderRepository.findAll().stream().map(this::mapToOrderResponse).toList(), HttpStatus.OK);
    }

    public ResponseEntity<String> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<Item> items = orderRequest.getItems()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setItems(items);
        items.forEach(item -> item.setOrder(order));

        List<Inventory> inventoryList = order.getItems().stream()
                .map(this::mapToInventory)
                .toList();

        ResponseEntity<String> res = webClient.put()
                .uri("http://localhost:8082/api/inventory/stock")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(inventoryList))
                .retrieve()
                .toEntity(String.class)
                .block();

        if (res.getStatusCode().equals(HttpStatus.OK)) {
            orderRepository.save(order);
            return new ResponseEntity<>("Order Saved", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Order is not placed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Item mapToDto(ItemDto itemDto) {
        Item item = new Item();
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        item.setProductId(itemDto.getProductId());
        return item;
    }

    private ItemDto mapToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemDto.getId());
        itemDto.setPrice(item.getPrice());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setProductId(item.getProductId());
        return itemDto;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .items(order.getItems().stream().map(this::mapToDto).toList())
                .build();
    }

    private Inventory mapToInventory(Item item) {
        return Inventory.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .build();
    }
}