package com.oceanview.resort.dao;

import com.oceanview.resort.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {
  long create(Reservation reservation);

  Optional<Reservation> findById(long id);

  List<Reservation> findAll();

  List<Reservation> findByGuestId(long guestId);

  boolean existsOverlappingReservation(long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId);

  boolean update(Reservation reservation);

  boolean deleteById(long id);
}
