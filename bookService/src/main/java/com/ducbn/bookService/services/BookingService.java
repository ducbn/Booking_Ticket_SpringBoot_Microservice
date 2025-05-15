package com.ducbn.bookService.services;

import com.ducbn.bookService.dtos.BookingDTO;
import com.ducbn.bookService.models.Booking;
import com.ducbn.bookService.repositories.BookingRepository;
import com.ducbn.bookService.clients.BusClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final BusClient busClient;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // Kiểm tra ghế còn trống
        List<String> availableSeats = busClient.getAvailableSeats(bookingDTO.getBusId());
        String[] seats = bookingDTO.getSeatNumbers().split(",");

        for (String seat : seats) {
            if (!availableSeats.contains(seat)) {
                throw new RuntimeException("Seat " + seat + " is already booked.");
            }
        }

        // Tính tổng giá vé
        BigDecimal totalPrice = BigDecimal.valueOf(seats.length * 100);  // Ví dụ 100 là giá mỗi ghế

        // Lưu booking vào cơ sở dữ liệu
        Booking booking = Booking.builder()
                .userId(bookingDTO.getUserId())
                .busId(bookingDTO.getBusId())
                .seatNumbers(bookingDTO.getSeatNumbers())
                .totalPrice(totalPrice)
                .status(Booking.BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return BookingDTO.fromEntity(savedBooking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Booking is already cancelled.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Nếu muốn, gọi BusService để cập nhật ghế thành available
        seatClient.releaseSeat(booking.getBusId(), booking.getSeatNumber());
    }

}
