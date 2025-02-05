package com.hotel.booking.domain.room.repository;

import com.hotel.booking.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /*
    검색 조건에 해당하는 방을 찾는 쿼리입니다. 아래는 쿼리의 내용을 적었습니다.

    예약돼있는 방의 id값과, 방의 id를 join
    WHERE db내의 방의 수용 가능한 인원 수 >= 검색하는 방의 인원 수
    AND 해당하는 숙소의 방 수 >= 검색하는 방의 수
    AND ((예약이 없거나) (에약이 취소됐지만, 검색하는 날짜에 해당되거나) (검색하는 날짜에 해당되지 않거나))
     */
    @Query("SELECT r FROM Room r " +
            "LEFT JOIN Reservation re ON r.id = re.room.id " +
            "WHERE r.people >= :people " +
            "AND r.accommodation.roomCount >= :roomCount " +
            "AND (re IS NULL) " +
                "OR (re.status = 'CANCELLED' AND :checkInDate < re.checkOutDate AND :checkOutDate > re.checkInDate) " +
                "OR (re.checkInDate >= :checkOutDate OR re.checkOutDate <= :checkInDate)")
    List<Room> findMatchingRooms(Integer people, Integer roomCount, LocalDate checkInDate, LocalDate checkOutDate);
}
