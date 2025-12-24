package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.dto.HotelDto;
import com.shreyanshsinghks.airbnbapp.dto.HotelInfoDto;
import com.shreyanshsinghks.airbnbapp.entity.Hotel;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
