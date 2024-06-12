package com.example.HotelService.services;

import com.example.HotelService.entities.Hotel;

import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    Hotel getSingleHotel(String id);
}
