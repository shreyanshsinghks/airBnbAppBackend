package com.shreyanshsinghks.airbnbapp.dto;

import com.shreyanshsinghks.airbnbapp.entity.Hotel;
import com.shreyanshsinghks.airbnbapp.entity.Room;
import com.shreyanshsinghks.airbnbapp.entity.User;
import com.shreyanshsinghks.airbnbapp.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;
    private Integer roomCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
