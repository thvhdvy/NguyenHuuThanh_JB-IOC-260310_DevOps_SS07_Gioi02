package com.quickbite.restaurant_service.controller;

import com.quickbite.restaurant_service.entity.MenuItem;
import com.quickbite.restaurant_service.entity.Restaurant;
import com.quickbite.restaurant_service.repository.MenuItemRepository;
import com.quickbite.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

	private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

	public record OrderLine(Long menuItemId, int quantity) {
	}

	public record OrderNotification(Long orderId, List<OrderLine> items) {
	}

	public record OrderDecision(boolean accepted, String reason) {
	}

	public record AvailabilityRequest(boolean available) {
	}

	private final RestaurantRepository restaurants;
	private final MenuItemRepository menuItems;

	@GetMapping
	public List<Restaurant> list() {
		return restaurants.findAll();
	}

	@GetMapping("/{id}")
	public Restaurant get(@PathVariable Long id) {
		return restaurants.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found: " + id));
	}

	@GetMapping("/{id}/menu")
	public List<MenuItem> menu(@PathVariable Long id) {
		get(id);
		return menuItems.findByRestaurantId(id);
	}

	@PatchMapping("/{id}/menu/{itemId}")
	public MenuItem setAvailability(@PathVariable Long id, @PathVariable Long itemId,
			@RequestBody AvailabilityRequest req) {
		MenuItem item = menuItems.findByIdAndRestaurantId(itemId, id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found: " + itemId));
		item.setAvailable(req.available());
		return menuItems.save(item);
	}

	/** order-service gọi để báo nhà hàng chuẩn bị món; hết món thì từ chối. */
	@PostMapping("/{id}/orders")
	public OrderDecision receiveOrder(@PathVariable Long id, @RequestBody OrderNotification req) {
		get(id);
		for (OrderLine line : req.items()) {
			MenuItem item = menuItems.findByIdAndRestaurantId(line.menuItemId(), id).orElse(null);
			if (item == null || !item.isAvailable()) {
				String name = item == null ? "#" + line.menuItemId() : item.getName();
				log.info("Restaurant {} REJECTED order {}: out of stock {}", id, req.orderId(), name);
				return new OrderDecision(false, "Hết món: " + name);
			}
		}
		log.info("Restaurant {} ACCEPTED order {}", id, req.orderId());
		return new OrderDecision(true, "Accepted");
	}
}
