package com.hotel.booking.domain.accommodation.repository;

import com.hotel.booking.domain.accommodation.dto.AccommodationListResponseDto;
import com.hotel.booking.domain.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("SELECT new com.hotel.booking.domain.accommodation.dto.AccommodationListResponseDto(a, l) " +
        "FROM Accommodation a " +
        "JOIN Location l ON a.id = l.accommodation.id")
    List<AccommodationListResponseDto> findAllWithLocation();

}
