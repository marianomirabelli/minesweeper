package com.deviget.minesweeper.service.exception;

public class GameException extends RuntimeException {

    private String type;
    private String detail;
    private int status;

    public GameException(){}

    public GameException(String type, String detail, int status){
        super(detail);
        this.type = type;
        this.detail = detail;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public int getStatus() {
        return status;
    }
}
