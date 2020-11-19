package com.deviget.minesweeper.service.service;

import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.GameMove;

public interface GameService {

    String createNewGame(int row, int columns, int mines);

    Board makeMove(String gameId,GameMove movement);
}
