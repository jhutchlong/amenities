package edu.depaul.amenities.service;

import edu.depaul.amenities.domain.Amenity;
import edu.depaul.amenities.model.AmenityDTO;
import edu.depaul.amenities.repos.AmenityRepository;
import edu.depaul.amenities.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(final AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<AmenityDTO> findAll() {
        final List<Amenity> amenities = amenityRepository.findAll(Sort.by("id"));
        return amenities.stream()
                .map(amenity -> mapToDTO(amenity, new AmenityDTO()))
                .toList();
    }

    public AmenityDTO get(final Long id) {
        return amenityRepository.findById(id)
                .map(amenity -> mapToDTO(amenity, new AmenityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AmenityDTO amenityDTO) {
        final Amenity amenity = new Amenity();
        mapToEntity(amenityDTO, amenity);
        return amenityRepository.save(amenity).getId();
    }

    public void update(final Long id, final AmenityDTO amenityDTO) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(amenityDTO, amenity);
        amenityRepository.save(amenity);
    }

    public void delete(final Long id) {
        amenityRepository.deleteById(id);
    }

    private AmenityDTO mapToDTO(final Amenity amenity, final AmenityDTO amenityDTO) {
        amenityDTO.setId(amenity.getId());
        amenityDTO.setName(amenity.getName());
        amenityDTO.setType(amenity.getType());
        amenityDTO.setCapacity(amenity.getCapacity());
        return amenityDTO;
    }

    private Amenity mapToEntity(final AmenityDTO amenityDTO, final Amenity amenity) {
        amenity.setName(amenityDTO.getName());
        amenity.setType(amenityDTO.getType());
        amenity.setCapacity(amenityDTO.getCapacity());
        return amenity;
    }

    public boolean nameExists(final String name) {
        return amenityRepository.existsByNameIgnoreCase(name);
    }

}
