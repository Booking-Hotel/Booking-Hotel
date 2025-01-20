package com.hotel.booking.domain.accommodation.service;

import com.hotel.booking.domain.accommodation.dto.AccommodationListResponseDto;
import com.hotel.booking.domain.accommodation.repository.AccommodationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public List<AccommodationListResponseDto> findAllAccommodations() {
        return accommodationRepository.findAllWithLocation();
    }

}
