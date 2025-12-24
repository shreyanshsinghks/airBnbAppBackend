package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.dto.BookingDto;
import com.shreyanshsinghks.airbnbapp.dto.BookingRequest;
import com.shreyanshsinghks.airbnbapp.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
