package com.shreyanshsinghks.airbnbapp.service;

import com.shreyanshsinghks.airbnbapp.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);

}
