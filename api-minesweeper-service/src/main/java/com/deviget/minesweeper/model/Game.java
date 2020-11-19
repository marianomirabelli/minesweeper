package com.deviget.minesweeper.model;

public class Game {

    private String id;
    private Board board;
    private GameStatus status;

    public Game(Board board){
        this.board = board;
        this.status = GameStatus.PLAYING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }


}
