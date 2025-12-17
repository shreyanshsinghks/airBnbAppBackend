package com.shreyanshsinghks.airbnbapp.repository;

import com.shreyanshsinghks.airbnbapp.entity.Inventory;
import com.shreyanshsinghks.airbnbapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    void deleteByDateAfterAndRoom(LocalDate dateAfter, Room room);
}
