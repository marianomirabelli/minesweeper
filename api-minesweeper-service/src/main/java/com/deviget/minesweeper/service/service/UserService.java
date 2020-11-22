package com.deviget.minesweeper.service.service;

import com.deviget.minesweeper.service.model.User;

import java.util.Optional;

public interface UserService {

    User createNewUser(User user);

    Optional<User> findByUserName(String userHash);
}
