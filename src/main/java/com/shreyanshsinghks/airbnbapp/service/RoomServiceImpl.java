package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.dto.RoomDto;
import com.shreyanshsinghks.airbnbapp.entity.Hotel;
import com.shreyanshsinghks.airbnbapp.entity.Room;
import com.shreyanshsinghks.airbnbapp.exception.ResourceNotFoundException;
import com.shreyanshsinghks.airbnbapp.repository.HotelRepository;
import com.shreyanshsinghks.airbnbapp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final HotelRepository  hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));

        return hotel
                .getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room in hotel with room ID: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with room ID: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + roomId));

        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(roomId);
    }
}
