package com.hotel.booking.domain.location.repository;

import com.hotel.booking.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.accommodation.id IN :accommodationIds")
    List<Location> findByAccommodationIds(List<Long> accommodationIds);

}

