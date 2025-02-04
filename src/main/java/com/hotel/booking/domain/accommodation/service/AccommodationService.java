package com.hotel.booking.domain.accommodation.service;

import com.hotel.booking.domain.accommodation.dto.AccommodationListResponseDto;
import com.hotel.booking.domain.accommodation.dto.AccommodationSearchReqeustDto;
import com.hotel.booking.domain.accommodation.dto.AccommodationSearchResponseDto;
import com.hotel.booking.domain.accommodation.entity.Accommodation;
import com.hotel.booking.domain.accommodation.repository.AccommodationRepository;
import com.hotel.booking.domain.location.entity.Location;
import com.hotel.booking.domain.location.repository.LocationRepository;
import com.hotel.booking.domain.room.entity.Room;
import com.hotel.booking.domain.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final LocationRepository locationRepository;
    @Transactional
    public List<AccommodationListResponseDto> getAccommodationList() {
        return accommodationRepository.findAllWithLocation();
    }

    @Transactional
    public List<AccommodationSearchResponseDto> getAccommodationSearch(AccommodationSearchReqeustDto reqeustDto) {
        Integer people = reqeustDto.getPeople();
        Integer roomCount = reqeustDto.getRoomCount();
        LocalDate checkInDate = reqeustDto.getCheckInDate();
        LocalDate checkOutDate = reqeustDto.getCheckOutDate();
        String keyword = reqeustDto.getKeyword();

        // 검색 조건에 맞는 방 찾기
        List<Room> availableRooms = roomRepository.findMatchingRooms(people, roomCount, checkInDate, checkOutDate);
        List<Long> accommodationIds = availableRooms.stream()
                .map(room -> room.getAccommodation().getId())
                .distinct()
                .toList();

        // LIKE절을 사용하여 keyword로 검색
        List<Accommodation> accommodations = accommodationRepository.findByIdsAndKeywordWithLocation(accommodationIds, keyword);

        List<Location> locations = locationRepository.findByAccommodationIds(accommodationIds);

        // 숙소 id에 해당 하는 객실 List
        Map<Long, List<Room>> roomsByAccommodation = availableRooms.stream()
                .collect(Collectors.groupingBy(room -> room.getAccommodation().getId()));

        return accommodations.stream()
                .map(accommodation -> {
                    String address = locations.stream()
                            .map(Location::getAddress)
                            .findFirst()
                            .orElse("");  // 주소가 없는 경우 빈 문자열 반환

                    // 해당 숙소의 사용 가능한 객실들 중 최소 가격 찾기
                    Integer minPrice = roomsByAccommodation.getOrDefault(accommodation.getId(), Collections.emptyList())
                            .stream()
                            .map(Room::getPrice)
                            .min(Integer::compareTo)
                            .orElse(0);

                    return AccommodationSearchResponseDto.builder()
                            .accommodationId(accommodation.getId())
                            .name(accommodation.getName())
                            .star(accommodation.getStar())
                            .rating(accommodation.getRating())
                            .amenities(accommodation.getAmenities())
                            .price(minPrice)
                            .address(address)
                            .imgUrl(accommodation.getImgUrl())
                            .build();
                })
                .toList();
    }


}
