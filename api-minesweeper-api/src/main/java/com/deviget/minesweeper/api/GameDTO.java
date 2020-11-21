package com.deviget.minesweeper.api;

import java.util.Objects;

public class GameDTO {

    private String id;
    private BoardDTO board;
    private GameStatusDTO status;

    public GameDTO(){}

    public GameDTO(String id, BoardDTO board, GameStatusDTO status){
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameDTO)) return false;
        GameDTO gameDTO = (GameDTO) o;
        return Objects.equals(id, gameDTO.id) &&
                Objects.equals(board, gameDTO.board) &&
                status == gameDTO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board, status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GameDTO{");
        sb.append("id='").append(id).append('\'');
        sb.append(", board=").append(board);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
