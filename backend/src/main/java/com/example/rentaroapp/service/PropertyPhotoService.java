package com.example.rentaroapp.service;

import com.example.rentaroapp.exception.ResourceNotFoundException;
import com.example.rentaroapp.model.Property;
import com.example.rentaroapp.model.PropertyPhoto;
import com.example.rentaroapp.repository.PropertyPhotoRepository;
import com.example.rentaroapp.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyPhotoService {

    private final PropertyPhotoRepository propertyPhotoRepository;
    private final PropertyRepository propertyRepository;

    public PropertyPhoto addPhoto(Long propertyId, String url, Integer orderIndex) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        int maxOrder = propertyPhotoRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId)
                .stream()
                .mapToInt(PropertyPhoto::getOrderIndex)
                .max()
                .orElse(-1);

        PropertyPhoto photo = new PropertyPhoto();
        photo.setProperty(property);
        photo.setUrl(url);
        photo.setIsCover(false);
        photo.setOrderIndex(orderIndex != null ? orderIndex : maxOrder + 1);
        return propertyPhotoRepository.save(photo);
    }

    public PropertyPhoto setCoverPhoto(Long photoId) {
        PropertyPhoto photo = propertyPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + photoId));

        propertyPhotoRepository.findByPropertyIdOrderByOrderIndexAsc(photo.getProperty().getId())
                .stream()
                .filter(PropertyPhoto::getIsCover)
                .forEach(existingCover -> {
                    existingCover.setIsCover(false);
                    propertyPhotoRepository.save(existingCover);
                });

        photo.setIsCover(true);
        return propertyPhotoRepository.save(photo);
    }

    public void deletePhoto(Long photoId) {
        PropertyPhoto photo = propertyPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + photoId));
        propertyPhotoRepository.delete(photo);
    }

    public List<PropertyPhoto> getByPropertyId(Long propertyId) {
        return propertyPhotoRepository.findByPropertyIdOrderByOrderIndexAsc(propertyId);
    }
}
