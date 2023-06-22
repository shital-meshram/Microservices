package com.orderService.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.orderService.dto.InventoryResponse;
import com.orderService.dto.OrderLineItemsDto;
import com.orderService.dto.OrderRequest;
import com.orderService.model.Order;
import com.orderService.model.OrderLineItems;
import com.orderService.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient webClient;

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNum(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();

		order.setOrderLineItems(orderLineItems);
		
		List<String> skuCodes =  order.getOrderLineItems().stream()
		.map(OrderLineItems :: getSkuCode)
		.toList();
		//Call Inventory service and place order if order is in stock
		InventoryResponse[] inventoryResponseArray = webClient.get()
				.uri("http://localhost:8086/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve().bodyToMono(InventoryResponse[].class).block();
		
		boolean allProductInStoks = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse :: isInStock);
		
		if(allProductInStoks) {
			orderRepository.save(order);
		}else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
		
	}

	public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

		OrderLineItems orderLineItems = new OrderLineItems();

		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
