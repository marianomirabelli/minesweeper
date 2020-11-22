package com.deviget.minesweeper.service.service;

import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameMove;

import java.util.Optional;

public interface GameService {

    Game createNewGame(int row, int columns, int mines, String userName);

    Optional<Game> findByGameId(String id);

    Game makeMove(String gameId,GameMove movement, String userName);
}
