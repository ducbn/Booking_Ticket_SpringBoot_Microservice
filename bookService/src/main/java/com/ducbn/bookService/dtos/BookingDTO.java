package com.ducbn.bookService.dtos;

import com.ducbn.bookService.models.Booking;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
    private Long userId;
    private Long busId;
    private String seatNumbers;  // Danh sách ghế đã đặt
    private BigDecimal totalPrice;

    // Chuyển đổi từ Booking entity sang BookingDTO
    public static BookingDTO fromEntity(Booking booking) {
        return BookingDTO.builder()
                .userId(booking.getUserId())
                .busId(booking.getBusId())
                .seatNumbers(booking.getSeatNumbers())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}
