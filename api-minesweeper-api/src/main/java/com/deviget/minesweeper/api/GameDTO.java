package com.deviget.minesweeper.api;

import java.time.Instant;
import java.util.Objects;

public class GameDTO {

    private String id;
    private BoardDTO board;
    private Instant startTime;
    private Instant lastMove;
    private String userId;
    private GameStatusDTO status;

    public GameDTO() {
    }

    public GameDTO(String id, String userId, BoardDTO board,
                   Instant startTime, Instant lastMove, GameStatusDTO status) {
        this.id = id;
        this.userId = userId;
        this.board = board;
        this.status = status;
        this.startTime = startTime;
        this.lastMove = lastMove;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getLastMove() {
        return lastMove;
    }

    public void setLastMove(Instant lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof GameDTO))
            return false;
        GameDTO gameDTO = (GameDTO) o;
        return  Objects.equals(id, gameDTO.id) &&
                Objects.equals(board, gameDTO.board) &&
                Objects.equals(startTime, gameDTO.startTime) &&
                Objects.equals(lastMove, gameDTO.lastMove) &&
                Objects.equals(userId, gameDTO.userId) &&
                status == gameDTO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board, startTime, lastMove, userId, status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GameDTO{");
        sb.append("id='").append(id).append('\'');
        sb.append(", board=").append(board);
        sb.append(", startTime=").append(startTime);
        sb.append(", lastMove=").append(lastMove);
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
