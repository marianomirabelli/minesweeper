package com.deviget.minesweeper.service.repository;

import com.deviget.minesweeper.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByUserName(String userName);

}
