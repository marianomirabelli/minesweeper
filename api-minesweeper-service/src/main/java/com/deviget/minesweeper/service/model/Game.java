package com.deviget.minesweeper.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

@Document
public class Game {

    @Id
    private String id;
    private String userId;
    private Instant startTime;
    private Instant lastMove;
    private boolean hasMadeFirstMove;
    private Board board;
    private GameStatus status;

    public Game(){};

    public Game(Board board, String userId){
        this.board = board;
        this.userId = userId;
        this.hasMadeFirstMove = false;
        this.startTime = Instant.now();
        this.lastMove = Instant.now();
        this.status = GameStatus.PLAYING;
    }

    public String getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getLastMove() {
        return lastMove;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setLastMove(Instant lastMove) {
        this.lastMove = lastMove;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean hasMadeFirstMove() {
        return hasMadeFirstMove;
    }

    public void setHasMadeFirstMove(boolean hasMadeFirstMove) {
        this.hasMadeFirstMove = hasMadeFirstMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Game))
            return false;
        Game game = (Game) o;
        return  Objects.equals(id, game.id) &&
                Objects.equals(userId, game.userId);

    }

    @Override public int hashCode() {
        return Objects.hash(id, userId);
    }
}
