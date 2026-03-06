package com.oceanview.resort.dao;

import com.oceanview.resort.model.Bill;

import java.util.List;
import java.util.Optional;

public interface BillDAO {
  long create(Bill bill);

  Optional<Bill> findById(long id);

  Optional<Bill> findByReservationId(long reservationId);

  List<Bill> findAll();

  boolean update(Bill bill);

  boolean deleteById(long id);
}
