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

    // 숙박 시설의 이름과 위치의 주소를 LIKE절로 keyword로 검색하는 쿼리
    @Query("SELECT a FROM Accommodation a " +
            "JOIN Location l ON a.id = l.accommodation.id " +
            "WHERE a.id IN :accommodationIds " +
            "AND (a.name LIKE %:keyword% OR l.address LIKE %:keyword%)")
    List<Accommodation> findByIdsAndKeywordWithLocation(List<Long> accommodationIds, String keyword);

}
