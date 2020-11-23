package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.BoardDTO;
import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.GameStatusDTO;
import com.deviget.minesweeper.service.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameToGameDTOConverter implements Converter<Game, GameDTO> {

    private final BoardToBoardDTOConverter boardToBoardDTOConverter;

    @Autowired
    public GameToGameDTOConverter(BoardToBoardDTOConverter boardToBoardDTOConverter) {
        this.boardToBoardDTOConverter = boardToBoardDTOConverter;
    }

    @Override
    public GameDTO convert(Game game) {
        GameStatusDTO status = switch (game.getStatus()) {
            case WON -> GameStatusDTO.WON;
            case LOST -> GameStatusDTO.LOST;
            case PLAYING -> GameStatusDTO.PLAYING;
        };

        BoardDTO boardDTO = boardToBoardDTOConverter.convert(game.getBoard());

        return new GameDTO(game.getId(), game.getUserId(), boardDTO,
                game.getStartTime(), game.getLastMove(), status);

    }
}
