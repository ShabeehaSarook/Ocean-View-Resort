package com.oceanview.resort.dao;

import com.oceanview.resort.model.GuestProfile;

import java.util.List;
import java.util.Optional;

public interface GuestProfileDAO {
  long create(GuestProfile profile);

  Optional<GuestProfile> findById(long id);

  Optional<GuestProfile> findByUserId(long userId);

  List<GuestProfile> findAll();

  boolean update(GuestProfile profile);

  boolean deleteById(long id);
}
