package com.deviget.minesweeper.model;

public class Cell {

    private int row;
    private int column;
    private boolean mine;
    private boolean opened;


    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.mine = mine;
        this.opened = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }


}
