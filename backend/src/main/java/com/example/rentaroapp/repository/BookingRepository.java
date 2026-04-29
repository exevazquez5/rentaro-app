package com.example.rentaroapp.repository;

import com.example.rentaroapp.model.Booking;
import com.example.rentaroapp.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByPropertyId(Long propertyId);

    List<Booking> findByPropertyIdAndStatus(Long propertyId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND b.status != 'CANCELLED' AND b.checkIn < :checkOut AND b.checkOut > :checkIn")
    List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
