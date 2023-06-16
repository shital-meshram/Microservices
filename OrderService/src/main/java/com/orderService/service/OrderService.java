package com.orderService.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNum(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();

		order.setOrderLineItems(orderLineItems);
		
		orderRepository.save(order);
	}

	public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

		OrderLineItems orderLineItems = new OrderLineItems();

		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
