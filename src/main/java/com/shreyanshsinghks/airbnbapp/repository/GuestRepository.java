package com.shreyanshsinghks.airbnbapp.repository;

import com.shreyanshsinghks.airbnbapp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}