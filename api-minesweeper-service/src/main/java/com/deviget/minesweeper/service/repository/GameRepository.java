package com.deviget.minesweeper.service.repository;

import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface GameRepository extends MongoRepository<Game,String> {

    Game findGameByUserIdAndStatus(String userId, GameStatus status);
}
