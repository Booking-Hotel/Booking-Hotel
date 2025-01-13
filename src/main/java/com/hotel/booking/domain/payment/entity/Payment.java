package com.hotel.booking.domain.payment.entity;

import com.hotel.booking.domain.reservation.entity.Reservation;
import com.hotel.booking.domain.room.entity.Room;
import com.hotel.booking.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

}

