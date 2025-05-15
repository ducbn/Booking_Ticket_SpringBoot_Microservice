package com.ducbn.bookService.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bus_id", nullable = false)
    private Long busId;

    @Column(name = "seat_numbers", nullable = false)
    private String seatNumbers;  // Danh sách ghế đã đặt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;  // PENDING, CONFIRMED, CANCELLED

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}
