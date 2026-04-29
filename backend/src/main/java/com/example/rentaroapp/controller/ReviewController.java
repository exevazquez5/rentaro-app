package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.Review;
import com.example.rentaroapp.service.ReviewService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody CreateReviewRequest request) {
        Review review = reviewService.createReview(
                request.propertyId(),
                request.guestId(),
                request.bookingId(),
                request.rating(),
                request.comment()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getByProperty(propertyId));
    }

    @GetMapping("/property/{propertyId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getAverageRating(propertyId));
    }

    public record CreateReviewRequest(
            @NotNull Long propertyId,
            @NotNull Long guestId,
            @NotNull Long bookingId,
            @NotNull @Min(1) @Max(5) Integer rating,
            String comment
    ) {}
}
