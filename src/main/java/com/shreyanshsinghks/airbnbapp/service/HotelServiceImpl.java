package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.dto.HotelDto;
import com.shreyanshsinghks.airbnbapp.entity.Hotel;
import com.shreyanshsinghks.airbnbapp.entity.Room;
import com.shreyanshsinghks.airbnbapp.exception.ResourceNotFoundException;
import com.shreyanshsinghks.airbnbapp.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new Hotel with name {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        hotelRepository.save(hotel);
        log.info("Created a new Hotel with hotel id {}", hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel with id {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating Hotel with id {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotelRepository.save(hotel);
        log.info("Updated Hotel with hotel id {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));

        for (Room room:  hotel.getRooms()) {
            inventoryService.deleteFutureInventories(room);
        }
        hotelRepository.deleteById(hotelId);
    }

    @Override
    @Transactional
    public void activateHotel(long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
        log.info("Activating Hotel with hotel id {}", hotelId);
        hotel.setActive(true);

//        Assuming only do it ones
        for (Room room : hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }

    }
}
