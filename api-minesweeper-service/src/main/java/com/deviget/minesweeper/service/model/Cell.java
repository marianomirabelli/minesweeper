package com.deviget.minesweeper.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell {

    private int row;
    private int column;
    private int minesAround;
    private boolean mine;
    private boolean flipped;
    private boolean marked;
    private boolean flagged;
    private final List<Cell> neighbours;


    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.mine = false;
        this.flipped = false;
        this.minesAround = 0;
        this.neighbours = new ArrayList<>(8);
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

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    protected void addNeighbours(List<Cell> neighbours) {
        this.neighbours.addAll(neighbours);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                column == cell.column &&
                minesAround == cell.minesAround &&
                mine == cell.mine &&
                flipped == cell.flipped &&
                marked == cell.marked &&
                flagged == cell.flagged;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, minesAround, mine, flipped, marked, flagged);
    }
}