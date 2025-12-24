package com.shreyanshsinghks.airbnbapp.repository;

import com.shreyanshsinghks.airbnbapp.entity.Hotel;
import com.shreyanshsinghks.airbnbapp.entity.Inventory;
import com.shreyanshsinghks.airbnbapp.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);

    @Query(
            value = """
                    SELECT DISTINCT i.hotel
                    FROM Inventory i
                    WHERE i.city = :city
                        AND i.date BETWEEN :startDate AND :endDate
                        AND i.closed = false
                        AND (i.totalCount - i.bookedCount - i.reservedCount >= :roomCount)
                    GROUP BY i.hotel, i.room
                    HAVING COUNT(*) = :dateCount
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT i.hotel.id)
                    FROM Inventory i
                    WHERE i.city = :city
                        AND i.date BETWEEN :startDate AND :endDate
                        AND i.closed = false
                        AND (i.totalCount - i.bookedCount - i.reservedCount >= :roomCount)
                    """
    )
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );


    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
                        AND i.date BETWEEN :startDate AND :endDate
                        AND i.closed = false
                        AND (i.totalCount - i.bookedCount - i.reservedCount >= :roomCount)
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount
    );
}
