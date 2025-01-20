package com.hotel.booking.domain.accommodation.controller;

import com.hotel.booking.domain.accommodation.dto.AccommodationListResponseDto;
import com.hotel.booking.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/list")
    public List<AccommodationListResponseDto> findAll() {
        return accommodationService.findAllAccommodations();
    }

}