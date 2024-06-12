package com.example.UserService.services;

import com.example.UserService.entities.Hotel;
import com.example.UserService.entities.Rating;
import com.example.UserService.entities.User;
import com.example.UserService.exceptions.ResourceNotFoundException;
import com.example.UserService.external.services.HotelService;
import com.example.UserService.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;
    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            Rating[] userRatingsArray = restTemplate.getForObject(
                    "http://RATINGSERVICE/ratings/users/" + user.getUserId(), Rating[].class);

            if (userRatingsArray != null) {
                logger.info("{}", (Object) userRatingsArray);
                List<Rating> ratingList = Arrays.stream(userRatingsArray).map(rating -> {
                    Hotel hotel = hotelService.getHotel(rating.getHotelId());
                    if (hotel != null) {
                        rating.setHotel(hotel);
                    } else {
                        logger.warn("Failed to fetch hotel for ID: {}.", rating.getHotelId());
                    }
                    return rating;
                }).toList();
                user.setRatings(ratingList);
            }
            else {
                logger.warn("No ratings found for user with ID: {}", user.getUserId());
            }
        }
        return users;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User with given ID:" + userId + " not found on server."));
        Rating[] userRatings = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        if(userRatings != null)
        {
            logger.info("{}", userRatings);
            List<Rating> ratingList = Arrays.stream(userRatings).map(rating -> {
//                ResponseEntity<Hotel> entity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);
//                Hotel hotel = entity.getBody();

                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                if (hotel != null) {
                    rating.setHotel(hotel);
                } else {
                    logger.warn("Failed to fetch hotel for ID: {}.", rating.getHotelId());
                }
                return rating;
            }).toList();
            user.setRatings(ratingList);
        }
        else {
            logger.warn("No ratings found for user with ID: {}", user.getUserId());
        }
        return user;
    }
}
