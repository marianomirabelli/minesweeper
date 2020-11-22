package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.*;
import com.deviget.minesweeper.service.config.ConvertersConfiguration;
import com.deviget.minesweeper.service.config.WebConfiguration;
import com.deviget.minesweeper.service.converters.BoardToBoardDTOConverter;
import com.deviget.minesweeper.service.converters.CellToCellDTOConverter;
import com.deviget.minesweeper.service.converters.GameMoveDTOToGameMoveConverter;
import com.deviget.minesweeper.service.converters.GameToGameDTOConverter;
import com.deviget.minesweeper.service.exception.GameException;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = {WebConfiguration.class, ConvertersConfiguration.class,
                            GameToGameDTOConverter.class, BoardToBoardDTOConverter.class,
                            CellToCellDTOConverter.class, GameMoveDTOToGameMoveConverter.class,
                            GameController.class, GameControllerAdvisor.class})
@EnableWebMvc
@AutoConfigureMockMvc
public class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createNewGame() throws Exception {
        StartGameDTO startGameDTO = new StartGameDTO(3, 3, 2);
        Game game = new Game(Mockito.mock(Board.class),"fooUserId");
        String serializedDto = objectMapper.writeValueAsString(startGameDTO);

        Mockito.when(gameService.createNewGame(3, 3, 2, "fooUserId")).thenReturn(game);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-UserId", "fooUserId")
                .content(serializedDto))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andReturn();

        GameDTO gameDTO = objectMapper.readValue(result.getResponse().getContentAsString(),GameDTO.class);
        Mockito.verify(gameService,Mockito.times(1)).createNewGame(3,3,2,"fooUserId");
        Assertions.assertEquals("fooUserId",gameDTO.getUserId());
        Assertions.assertEquals(GameStatusDTO.PLAYING,gameDTO.getStatus());

    }

    @Test
    public void flagCellSuccessFully() throws Exception {
        GameMoveDTO gameMoveDTO = new GameMoveDTO(3, 3, GameActionDTO.FLAG);
        Game game = new Game(Mockito.mock(Board.class),"fooUserId");
        GameMove gameMove = new GameMove(3,3, GameAction.FLAG);
        String serializedDto = objectMapper.writeValueAsString(gameMoveDTO);

        Mockito.when(gameService.makeMove("fooId",gameMove)).thenReturn(game);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/game/fooId")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-UserId", "fooUserId")
                .content(serializedDto))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        GameDTO gameDTO = objectMapper.readValue(result.getResponse().getContentAsString(),GameDTO.class);
        Mockito.verify(gameService,Mockito.times(1)).makeMove("fooId",gameMove);
        Assertions.assertEquals("fooUserId",gameDTO.getUserId());
        Assertions.assertEquals(GameStatusDTO.PLAYING,gameDTO.getStatus());

    }


    @Test
    public void flagCellWithErrorFully() throws Exception {
        final String type = "ACTION_NOT_ALLOWED";
        final String detail = "Closed or marked cells can be flagged";
        final int code = 406;

        GameMoveDTO gameMoveDTO = new GameMoveDTO(3, 3, GameActionDTO.FLAG);
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(type,detail,code);
        GameMove gameMove = new GameMove(3,3, GameAction.FLAG);
        GameException gameException = new GameException(type,detail,code);
        String serializedDto = objectMapper.writeValueAsString(gameMoveDTO);

        Mockito.doThrow(gameException).when(gameService).makeMove("fooId",gameMove);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/game/fooId")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-UserId", "fooUserId")
                .content(serializedDto))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE.value()))
                .andReturn();

        ApiErrorDTO apiErrorObtained = objectMapper.readValue(result.getResponse().getContentAsString(),ApiErrorDTO.class);
        Mockito.verify(gameService,Mockito.times(1)).makeMove("fooId",gameMove);
        Assertions.assertEquals(apiErrorDTO,apiErrorObtained);

    }

}
