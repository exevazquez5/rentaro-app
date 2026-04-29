package com.example.rentaroapp.repository;

import com.example.rentaroapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPropertyId(Long propertyId);

    List<Review> findByGuestId(Long guestId);

    long countByPropertyId(Long propertyId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId")
    Double avgRatingByPropertyId(@Param("propertyId") Long propertyId);
}
