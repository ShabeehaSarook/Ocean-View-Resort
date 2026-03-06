package com.oceanview.resort.dao;

import com.oceanview.resort.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
  long create(User user);

  Optional<User> findById(long id);

  Optional<User> findByUsername(String username);

  List<User> findAll();

  boolean update(User user);

  boolean deleteById(long id);
}
