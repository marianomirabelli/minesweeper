package com.deviget.minesweeper.api;

public class GameDTO {

    private BoardDTO board;
    private GameStatusDTO status;

    public GameDTO(){}

    public GameDTO(BoardDTO board, GameStatusDTO status){
        this.board = board;
        this.status = status;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public void setBoard(BoardDTO board) {
        this.board = board;
    }

    public GameStatusDTO getStatus() {
        return status;
    }

    public void setStatus(GameStatusDTO status) {
        this.status = status;
    }
}
