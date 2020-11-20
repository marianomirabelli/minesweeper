package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.service.GameService;
import com.deviget.minesweeper.service.util.BoardUtils;
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
        String id = repository.save(game).getId();
        return id;
    }

    @Override
    public Board makeMove(String gameId, GameMove move) {
        Game game = repository.findById(gameId).get();
        Board board = game.getBoard();
        Cell cell = board.getCells()[move.getRow()][move.getColumn()];
        switch (move.getAction()){
            case FLAG -> cell.updateStatus(CellState.FLAGGED);
            case MARK -> cell.updateStatus(CellState.MARKED);
            case FLIP -> BoardUtils.floodFlip(board,cell);

        }
        return repository.save(game).getBoard();
    }
}
