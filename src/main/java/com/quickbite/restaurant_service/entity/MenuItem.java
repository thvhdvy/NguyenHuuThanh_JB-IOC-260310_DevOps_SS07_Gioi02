package com.quickbite.restaurant_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long restaurantId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private boolean available;
}
