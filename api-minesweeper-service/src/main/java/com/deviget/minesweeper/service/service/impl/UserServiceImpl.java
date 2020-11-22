package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.model.User;
import com.deviget.minesweeper.service.repository.UserRepository;
import com.deviget.minesweeper.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(User user) {
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
