package com.deviget.minesweeper.service.impl;

import com.deviget.minesweeper.model.Board;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public String createNewGame(int row, int columns, int mines) {

        Board board = new Board(row,columns,mines);
        Game game = new Game(board);
        return game.getId();
    }
}
