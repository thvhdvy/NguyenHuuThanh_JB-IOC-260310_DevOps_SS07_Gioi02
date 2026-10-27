package com.quickbite.restaurant_service.repository;

import com.quickbite.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
