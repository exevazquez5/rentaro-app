package com.example.rentaroapp.controller;

import com.example.rentaroapp.model.Booking;
import com.example.rentaroapp.service.BookingService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody CreateBookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.propertyId(),
                request.guestId(),
                request.checkIn(),
                request.checkOut()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Booking>> getByGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(bookingService.getByGuest(guestId));
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Booking>> getByHost(@PathVariable Long hostId) {
        return ResponseEntity.ok(bookingService.getByHost(hostId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    public record CreateBookingRequest(
            @NotNull Long propertyId,
            @NotNull Long guestId,
            @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {}
}
