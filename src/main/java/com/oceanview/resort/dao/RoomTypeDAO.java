package com.oceanview.resort.dao;

import com.oceanview.resort.model.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeDAO {
  long create(RoomType roomType);

  Optional<RoomType> findById(long id);

  List<RoomType> findAll();

  boolean update(RoomType roomType);

  boolean deleteById(long id);
}
