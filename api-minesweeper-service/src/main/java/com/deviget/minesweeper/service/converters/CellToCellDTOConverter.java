package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.CellDTO;
import com.deviget.minesweeper.api.CellStateDTO;
import com.deviget.minesweeper.service.model.Cell;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CellToCellDTOConverter implements Converter<Cell, CellDTO> {

    @Override
    public CellDTO convert(Cell cell) {
        int rowDto = cell.getRow();
        int columnDto = cell.getColumn();
        int minesDto = cell.getMinesAround();
        CellStateDTO cellStateDTO = switch (cell.getState()){
            case OPENED -> convertOpenedCell(cell);
            case FLAGGED -> CellStateDTO.FLAGGED;
            case MARKED -> CellStateDTO.MARKED;
            case CLOSED -> CellStateDTO.CLOSED;
        };

        return new CellDTO(rowDto,columnDto,minesDto,cellStateDTO);

    }

    private CellStateDTO convertOpenedCell(Cell cell){

        if(cell.getMinesAround()==0){
            return CellStateDTO.BLANK;
        }else{
            return CellStateDTO.NUMBERED;
        }


    }
}
