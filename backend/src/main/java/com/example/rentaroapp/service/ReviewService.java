package com.example.rentaroapp.service;

import com.example.rentaroapp.exception.ResourceNotFoundException;
import com.example.rentaroapp.model.Booking;
import com.example.rentaroapp.model.BookingStatus;
import com.example.rentaroapp.model.Property;
import com.example.rentaroapp.model.Review;
import com.example.rentaroapp.model.User;
import com.example.rentaroapp.repository.BookingRepository;
import com.example.rentaroapp.repository.PropertyRepository;
import com.example.rentaroapp.repository.ReviewRepository;
import com.example.rentaroapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public Review createReview(Long propertyId, Long guestId, Long bookingId, Integer rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        User guest = userRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + guestId));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Can only review confirmed bookings");
        }

        if (!booking.getProperty().getId().equals(propertyId)) {
            throw new IllegalArgumentException("Booking does not belong to this property");
        }

        if (!booking.getGuest().getId().equals(guestId)) {
            throw new IllegalArgumentException("Booking does not belong to this guest");
        }

        if (reviewRepository.findByPropertyId(propertyId).stream()
                .anyMatch(r -> r.getBooking().getId().equals(bookingId))) {
            throw new IllegalArgumentException("Booking already has a review");
        }

        Review review = new Review();
        review.setProperty(property);
        review.setGuest(guest);
        review.setBooking(booking);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public List<Review> getByProperty(Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId);
    }

    public Double getAverageRating(Long propertyId) {
        Double avg = reviewRepository.avgRatingByPropertyId(propertyId);
        return avg != null ? avg : 0.0;
    }
}
