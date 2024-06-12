package com.example.UserService;

import com.example.UserService.entities.Rating;
import com.example.UserService.external.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest
@EnableFeignClients(basePackages = "com.example.UserService.services")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RatingService ratingService;

	@Test
	void createRating()
	{
		Rating rating = Rating.builder()
				.rating(10)
				.userId("")
				.hotelId("")
				.feedback("This is created using Feign client.")
				.build();
		Rating savedRating = ratingService.createRating(rating);

		System.out.println("New rating created");
	}
}
