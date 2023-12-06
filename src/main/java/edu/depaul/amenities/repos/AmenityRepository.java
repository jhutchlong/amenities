package edu.depaul.amenities.repos;

import edu.depaul.amenities.domain.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    boolean existsByNameIgnoreCase(String name);

}
