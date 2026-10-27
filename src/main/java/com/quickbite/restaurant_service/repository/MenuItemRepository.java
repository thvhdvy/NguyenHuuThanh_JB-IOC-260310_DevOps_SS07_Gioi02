package com.quickbite.restaurant_service.repository;

import com.quickbite.restaurant_service.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

	List<MenuItem> findByRestaurantId(Long restaurantId);

	Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);
}
