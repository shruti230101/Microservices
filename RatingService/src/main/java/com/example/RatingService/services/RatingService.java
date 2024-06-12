package com.example.RatingService.services;

import com.example.RatingService.entities.Rating;

import java.util.List;

public interface RatingService {

    Rating createRating(Rating rating);
    List<Rating> getAllRatings();
    List<Rating> getRatingByUserId(String userId);
    List<Rating> getRatingByHotelId(String hotelId);
}
