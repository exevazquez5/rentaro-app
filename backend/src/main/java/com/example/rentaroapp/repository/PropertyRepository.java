package com.example.rentaroapp.repository;

import com.example.rentaroapp.model.BookingStatus;
import com.example.rentaroapp.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByHostId(Long hostId);

    @Query("SELECT p FROM Property p WHERE LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Property> searchByCity(@Param("city") String city);

    @Query("SELECT p FROM Property p WHERE p.id NOT IN (" +
           "SELECT b.property.id FROM Booking b " +
           "WHERE b.status != 'CANCELLED' AND b.checkIn < :checkOut AND b.checkOut > :checkIn)")
    List<Property> findAvailableBetweenDates(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("SELECT p FROM Property p WHERE LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')) AND p.id NOT IN (" +
           "SELECT b.property.id FROM Booking b " +
           "WHERE b.status != 'CANCELLED' AND b.checkIn < :checkOut AND b.checkOut > :checkIn)")
    List<Property> searchByCityAndDates(@Param("city") String city, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
