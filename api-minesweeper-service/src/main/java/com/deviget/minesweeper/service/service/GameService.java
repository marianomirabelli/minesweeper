package com.deviget.minesweeper.service.service;

import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameMove;

public interface GameService {

    Game createNewGame(int row, int columns, int mines);

    Game makeMove(String gameId,GameMove movement);
}
