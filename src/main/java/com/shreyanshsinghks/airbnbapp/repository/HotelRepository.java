package com.shreyanshsinghks.airbnbapp.repository;

import com.shreyanshsinghks.airbnbapp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
