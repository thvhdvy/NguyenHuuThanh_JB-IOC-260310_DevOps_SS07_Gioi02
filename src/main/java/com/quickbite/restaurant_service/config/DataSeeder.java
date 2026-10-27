package com.quickbite.restaurant_service.config;

import com.quickbite.restaurant_service.entity.MenuItem;
import com.quickbite.restaurant_service.entity.Restaurant;
import com.quickbite.restaurant_service.repository.MenuItemRepository;
import com.quickbite.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

	private final RestaurantRepository restaurants;
	private final MenuItemRepository menuItems;

	@Override
	public void run(String... args) {
		if (restaurants.count() > 0) {
			return;
		}
		Restaurant pho = restaurants.save(new Restaurant(null, "Phở Thìn", "13 Lò Đúc, Hà Nội"));
		item(pho, "Phở bò tái", "55000", true);
		item(pho, "Phở gà", "50000", true);
		item(pho, "Bún bò Huế", "60000", false); // hết món để thử luồng bị từ chối

		Restaurant com = restaurants.save(new Restaurant(null, "Cơm Tấm Sài Gòn", "25 Nguyễn Huệ, TP.HCM"));
		item(com, "Cơm sườn bì chả", "45000", true);
		item(com, "Cơm gà nướng", "48000", true);
	}

	private void item(Restaurant r, String name, String price, boolean available) {
		menuItems.save(new MenuItem(null, r.getId(), name, new BigDecimal(price), available));
	}
}
