package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.GameMoveDTO;
import com.deviget.minesweeper.service.model.GameAction;
import com.deviget.minesweeper.service.model.GameMove;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameMoveDTOToGameMoveConverter implements Converter<GameMoveDTO, GameMove> {

    @Override
    public GameMove convert(GameMoveDTO gameMoveDTO) {
        int row = gameMoveDTO.getRow();
        int column = gameMoveDTO.getColumn();
        GameAction action = switch (gameMoveDTO.getAction()){
            case MARK -> GameAction.MARK;
            case FLAG -> GameAction.FLAG;
            case REMOVE_TAG -> GameAction.REMOVE_TAG;
            case FLIP -> GameAction.FLIP;
        };

        return new GameMove(row,column,action);
    }
}
