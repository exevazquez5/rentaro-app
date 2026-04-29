package com.example.rentaroapp.service;

import com.example.rentaroapp.exception.ResourceNotFoundException;
import com.example.rentaroapp.model.Booking;
import com.example.rentaroapp.model.BookingStatus;
import com.example.rentaroapp.model.Property;
import com.example.rentaroapp.model.User;
import com.example.rentaroapp.repository.BookingRepository;
import com.example.rentaroapp.repository.PropertyRepository;
import com.example.rentaroapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public Booking createBooking(Long propertyId, Long guestId, LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        User guest = userRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + guestId));

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(propertyId, checkIn, checkOut);
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Property is not available for the selected dates");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice = property.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getByGuest(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getByHost(Long hostId) {
        List<com.example.rentaroapp.model.Property> properties = propertyRepository.findByHostId(hostId);
        return properties.stream()
                .flatMap(p -> bookingRepository.findByPropertyId(p.getId()).stream())
                .toList();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }
}
