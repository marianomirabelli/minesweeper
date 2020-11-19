package com.deviget.minesweeper.service.model;

public class Cell {

    private int row;
    private int column;
    private int minesAround;
    private boolean mine;
    private boolean flipped;
    private boolean marked;
    private boolean flagged;


    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.mine = false;
        this.flipped = false;
        this.minesAround = 0;
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

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public void incrementMinesAround(){
        this.minesAround+=1;
    }
}
