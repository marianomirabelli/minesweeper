package com.deviget.minesweeper.api;

import java.util.Objects;

public class CellDTO {

    private int row;
    private int column;
    private int minesAround;
    private CellStateDTO state;

    public CellDTO(){}

    public CellDTO(int row, int column, int minesAround, CellStateDTO state){
        this.row = row;
        this.column = column;
        this.minesAround = minesAround;
        this.state =state;
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

    public int getMinesAround() {
        return minesAround;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public CellStateDTO getState() {
        return state;
    }

    public void setState(CellStateDTO state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellDTO)) return false;
        CellDTO cellDTO = (CellDTO) o;
        return row == cellDTO.row &&
                column == cellDTO.column &&
                minesAround == cellDTO.minesAround &&
                state == cellDTO.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, minesAround, state);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CellDTO{");
        sb.append("row=").append(row);
        sb.append(", column=").append(column);
        sb.append(", minesAround=").append(minesAround);
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
