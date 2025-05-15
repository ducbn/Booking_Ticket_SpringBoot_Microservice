package com.ducbn.bookService.repositories;

import com.ducbn.bookService.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Tìm kiếm booking theo ID người dùng và trạng thái
    Booking findByUserIdAndStatus(Long userId, Booking.BookingStatus status);
}