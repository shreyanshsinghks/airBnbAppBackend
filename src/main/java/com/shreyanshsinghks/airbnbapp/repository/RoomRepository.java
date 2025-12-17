package com.shreyanshsinghks.airbnbapp.repository;

import com.shreyanshsinghks.airbnbapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {

}
