package com.orderService.model;

import java.math.BigDecimal;

import javax.persistence.*;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="t_order_line_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderLineItems {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String skuCode;
	private BigDecimal price;
	private Integer quantity;
	

}
