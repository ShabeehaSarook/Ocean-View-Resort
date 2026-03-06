package com.oceanview.resort.dao;

import com.oceanview.resort.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomDAO {
  long create(Room room);

  Optional<Room> findById(long id);

  Optional<Room> findByRoomNumber(String roomNumber);

  List<Room> findAll();

  boolean update(Room room);

  boolean deleteById(long id);
}
