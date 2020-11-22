package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.model.User;
import com.deviget.minesweeper.service.repository.UserRepository;
import com.deviget.minesweeper.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(User user) {
        User createdUser = userRepository.save(user);
        logger.info("User {} has been created successfully",user.getUserName());
        return createdUser;
    }

    public Optional<User> findByUserName(String userName) {
        Optional<User> op =userRepository.findByUserName(userName);
        op.ifPresent((o)->logger.warn("User {} does not exist",userName));
        return op;
    }
}
