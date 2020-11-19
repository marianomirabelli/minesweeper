package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameMove;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.service.GameService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    public GameRepository repository;

    public GameServiceImpl(GameRepository repository){
        this.repository = repository;
    }

    @Override
    public String createNewGame(int row, int columns, int mines) {

        Board board = new Board(row,columns,mines);
        Game game = new Game(board);
        return game.getId();
    }

    @Override
    public Board makeMove(String gameId, GameMove move) {
        Game game = repository.findById(gameId).get();
        Board board = game.getBoard();
        switch (move.getAction()){
            case FLAG -> board.getCells()[move.getRow()][move.getColumn()].setFlagged(true);
            case MARK -> board.getCells()[move.getRow()][move.getColumn()].setMarked(true);
        }
        return repository.save(game).getBoard();
    }
}
