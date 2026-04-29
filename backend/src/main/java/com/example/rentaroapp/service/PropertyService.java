package com.example.rentaroapp.service;

import com.example.rentaroapp.exception.ResourceNotFoundException;
import com.example.rentaroapp.model.Property;
import com.example.rentaroapp.model.PropertyType;
import com.example.rentaroapp.model.User;
import com.example.rentaroapp.repository.PropertyRepository;
import com.example.rentaroapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public Property create(Long hostId, String title, String description, String address, String city,
                           String country, BigDecimal pricePerNight, Integer maxGuests, Integer bedrooms,
                           Integer bathrooms, PropertyType propertyType) {
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Host not found with id: " + hostId));

        Property property = new Property();
        property.setHost(host);
        property.setTitle(title);
        property.setDescription(description);
        property.setAddress(address);
        property.setCity(city);
        property.setCountry(country);
        property.setPricePerNight(pricePerNight);
        property.setMaxGuests(maxGuests);
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setPropertyType(propertyType);
        return propertyRepository.save(property);
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }

    public List<Property> searchByCity(String city) {
        return propertyRepository.searchByCity(city);
    }

    public List<Property> searchByCityAndDates(String city, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
        return propertyRepository.searchByCityAndDates(city, checkIn, checkOut);
    }

    public List<Property> listByHost(Long hostId) {
        return propertyRepository.findByHostId(hostId);
    }

    public Property update(Long id, String title, String description, BigDecimal pricePerNight,
                           Integer maxGuests, Integer bedrooms, Integer bathrooms, PropertyType propertyType) {
        Property property = findById(id);
        if (title != null) property.setTitle(title);
        if (description != null) property.setDescription(description);
        if (pricePerNight != null) property.setPricePerNight(pricePerNight);
        if (maxGuests != null) property.setMaxGuests(maxGuests);
        if (bedrooms != null) property.setBedrooms(bedrooms);
        if (bathrooms != null) property.setBathrooms(bathrooms);
        if (propertyType != null) property.setPropertyType(propertyType);
        return propertyRepository.save(property);
    }

    public void delete(Long id) {
        Property property = findById(id);
        propertyRepository.delete(property);
    }
}
