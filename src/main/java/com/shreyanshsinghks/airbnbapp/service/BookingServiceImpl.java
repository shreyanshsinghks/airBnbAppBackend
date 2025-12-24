package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.dto.BookingDto;
import com.shreyanshsinghks.airbnbapp.dto.BookingRequest;
import com.shreyanshsinghks.airbnbapp.dto.GuestDto;
import com.shreyanshsinghks.airbnbapp.entity.*;
import com.shreyanshsinghks.airbnbapp.entity.enums.BookingStatus;
import com.shreyanshsinghks.airbnbapp.exception.ResourceNotFoundException;
import com.shreyanshsinghks.airbnbapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
        log.info("Initialising booking for hotel with id {}, room with id {} and date {} - {}", bookingRequest.getHotelId(), bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(
                () -> new ResourceNotFoundException("Hotel with id " + bookingRequest.getHotelId() + " not found")
        );

        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room with id " + bookingRequest.getRoomId() + " not found")
        );

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomCount()
        );

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) + 1;
        if (inventoryList.size() != daysCount) {
            throw new IllegalStateException("Room is not available anymore");
        }

//        Reserve the room / update the booked count of inventories
        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomCount());
        }

        inventoryRepository.saveAll(inventoryList);


//        TODO: Calculate dynamic price

//        Create the booking
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(bookingRequest.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();

        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guests for bookingId {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Booking with id " + bookingId + " not found"));
        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }

        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not under RESERVED status, cannot  add guests");
        }

        for (GuestDto guestDto : guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);

        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusHours(10).isBefore(LocalDateTime.now());
    }


    public User getCurrentUser() {
        User user = new User();
        user.setId(1L); // TODO: Remove dummy user
        return user;
    }

}
