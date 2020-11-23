package com.deviget.minesweeper.service.service;

import com.deviget.minesweeper.service.App;
import com.deviget.minesweeper.service.exception.MinesweeperException;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.repository.UserRepository;
import com.deviget.minesweeper.service.service.impl.GameServiceImpl;
import com.deviget.minesweeper.service.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;
import java.util.UUID;

@EnableConfigurationProperties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
               classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameServiceTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        User user = new User("fooUserName");
        userRepository.save(user);
    }

    @Test
    public void createNewGameSuccessfully() {
        String userId = userRepository.findByUserName("fooUserName").get().getId();
        Game game = gameService.createNewGame(3, 3, 2, "fooUserName");
        Board board = game.getBoard();
        Assertions.assertNotNull(game);
        Assertions.assertFalse(game.hasMadeFirstMove());
        Assertions.assertEquals(userId, game.getUserId());
        Assertions.assertEquals(0, board.getMines().size());

    }

    @Test
    public void createNewGameFailsUserDoesNotExists() {
        MinesweeperException userException = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.createNewGame(3, 3, 2, "barUserName");
        });
        Assertions.assertEquals(userException.getType(), "NON_EXISTENT_USER");
        Assertions.assertEquals(userException.getStatus(), 401);
        Assertions.assertEquals(userException.getDetail(), "The username does not exists");
    }

    @Test
    public void createNewGameFailsDueBadBoardConfiguration() {
        String userId = userRepository.findByUserName("fooUserName").get().getId();
        Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.createNewGame(2, 2, 1, "fooUserName");
        });
        MinesweeperException badRowColumnsException = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.createNewGame(31, 31, 30, "fooUserName");
        });
        MinesweeperException badMinesConfigurationException = Assertions
                .assertThrows(MinesweeperException.class, () -> {
                    gameService.createNewGame(5, 5, 19, "fooUserName");
                });
        Assertions.assertEquals(badRowColumnsException.getType(), "ROW_COLUMNS_OUT_OF_RANGE");
        Assertions.assertEquals(badRowColumnsException.getStatus(), 412);
        Assertions.assertEquals(badRowColumnsException.getDetail(),
                "Rows and columns must be set in a range of 3 and 30");

        Assertions.assertEquals(badMinesConfigurationException.getType(), "MINES_NUMBER_EXCEEDED");
        Assertions.assertEquals(badMinesConfigurationException.getStatus(), 412);
        Assertions.assertEquals(badMinesConfigurationException.getDetail(),
                "The number of mines must be less than or equal to twenty percent of the squares on the board");
    }

    @Test
    public void makeFirstMove() {
        Cell[][] cells = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = { { 4, 2 }, { 4, 4 } };
        Board board = new Board(cells, minesCoordinates);
        User user = userRepository.findByUserName("fooUserName").get();
        Game game = new Game(board, user.getId());
        game = gameRepository.save(game);
        GameMove move = new GameMove(0, 0, GameAction.FLIP);
        Game gameAfterFirstMove = gameService.makeMove(game.getId(), move, user.getUserName());
        Assertions.assertEquals(gameAfterFirstMove.hasMadeFirstMove(), true);
        Assertions.assertEquals(gameAfterFirstMove.getStatus(), GameStatus.PLAYING);
        Assertions.assertEquals(2, gameAfterFirstMove.getBoard().getMines().size());
        assertCellStatus(CellState.CLOSED, gameAfterFirstMove.getBoard().getMines());
    }

    @Test
    public void userNotAllowedToPlayThisGame() {
        Cell[][] cells = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = { { 1, 2 }, { 2, 2 } };
        Board board = new Board(cells, minesCoordinates);
        User user = userRepository.findByUserName("fooUserName").get();
        Game game = new Game(board, UUID.randomUUID().toString());
        game = gameRepository.save(game);
        final String gameId = game.getId();
        MinesweeperException userNotAllowedException = Assertions
                .assertThrows(MinesweeperException.class, () -> {
                    gameService.makeMove(gameId, new GameMove(0, 0, GameAction.FLIP), "fooUserName");
                });
        Assertions.assertEquals(userNotAllowedException.getType(), "USER_NOT_AUTHORIZED_TO_THIS_GAME");
        Assertions.assertEquals(userNotAllowedException.getStatus(), 403);
        Assertions.assertEquals(userNotAllowedException.getDetail(), "The user cannot perform actions on this game");

    }

    @Test
    public void winGame() {
        User user = userRepository.findByUserName("fooUserName").get();
        Cell[][] cells = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = { { 1, 2 }, { 2, 2 } };
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board, user.getId());
        game.setHasMadeFirstMove(true);
        game = gameRepository.save(game);
        String gameId = game.getId();

        GameStatus gameStatusMove1 = gameService
                .makeMove(gameId, new GameMove(0, 0, GameAction.FLIP), user.getUserName()).getStatus();

        GameStatus gameStatusMove2 = gameService
                .makeMove(gameId, new GameMove(0, 2, GameAction.FLIP), user.getUserName()).getStatus();

        Assertions.assertEquals(GameStatus.PLAYING, gameStatusMove1);
        Assertions.assertEquals(GameStatus.WON, gameStatusMove2);
    }

    @Test
    public void doOtherActions() {
        User user = userRepository.findByUserName("fooUserName").get();
        Cell[][] cells = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = { { 1, 2 }, { 2, 2 } };
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board, user.getId());
        game.setHasMadeFirstMove(true);
        game = gameRepository.save(game);
        Game gameAfterFlagged = gameService.makeMove(game.getId(), new GameMove(0, 0, GameAction.FLAG), "fooUserName");
        GameStatus flagged = gameAfterFlagged.getStatus();
        Game gameAfterMarked = gameService.makeMove(game.getId(), new GameMove(0, 0, GameAction.MARK), "fooUserName");
        GameStatus markFlagged = gameAfterMarked.getStatus();
        Game gameAfterRemoveTags = gameService
                .makeMove(game.getId(), new GameMove(0, 0, GameAction.REMOVE_TAG), "fooUserName");
        GameStatus removedTags = gameAfterMarked.getStatus();
        Assertions.assertEquals(GameStatus.PLAYING, flagged);
        Assertions.assertEquals(CellState.FLAGGED, gameAfterFlagged.getBoard().getCells()[0][0].getState());
        Assertions.assertEquals(GameStatus.PLAYING, markFlagged);
        Assertions.assertEquals(CellState.MARKED, gameAfterMarked.getBoard().getCells()[0][0].getState());
        Assertions.assertEquals(GameStatus.PLAYING, removedTags);
        Assertions.assertEquals(CellState.CLOSED, gameAfterRemoveTags.getBoard().getCells()[0][0].getState());
    }

    @Test
    public void loseGame() {
        User user = userRepository.findByUserName("fooUserName").get();
        Cell[][] cells = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = { { 1, 2 }, { 2, 2 } };
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board, user.getId());
        game.setHasMadeFirstMove(true);
        game = gameRepository.save(game);
        String gameId = game.getId();

        GameStatus gameStatusMove1 = gameService
                .makeMove(gameId, new GameMove(0, 0, GameAction.FLIP), user.getUserName()).getStatus();

        GameStatus gameStatusMove2 = gameService
                .makeMove(gameId, new GameMove(1, 2, GameAction.FLIP), user.getUserName()).getStatus();

        Assertions.assertEquals(GameStatus.PLAYING, gameStatusMove1);
        Assertions.assertEquals(GameStatus.LOST, gameStatusMove2);
    }

    @Test
    public void actionsNotAllowed() {

        User user = userRepository.findByUserName("fooUserName").get();
        Cell[][] cells = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = { { 1, 2 }, { 2, 2 } };
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board, user.getId());
        game.setHasMadeFirstMove(true);
        game = gameRepository.save(game);
        String gameId = game.getId();

        gameService.makeMove(gameId, new GameMove(0, 0, GameAction.FLIP), user.getUserName()).getStatus();

        MinesweeperException flagNotAllowed = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.makeMove(gameId, new GameMove(0, 1, GameAction.FLAG), "fooUserName");
        });

        MinesweeperException markNotAllowed = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.makeMove(gameId, new GameMove(1, 1, GameAction.MARK), "fooUserName");
        });

        MinesweeperException flipNotAllowed = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.makeMove(gameId, new GameMove(0, 1, GameAction.FLIP), "fooUserName");
        });

        MinesweeperException removeTagsAllowed = Assertions.assertThrows(MinesweeperException.class, () -> {
            gameService.makeMove(gameId, new GameMove(1, 2, GameAction.REMOVE_TAG), "fooUserName");
        });

        Assertions.assertEquals(flipNotAllowed.getType(), "ACTION_NOT_ALLOWED");
        Assertions.assertEquals(flipNotAllowed.getStatus(), 406);
        Assertions.assertEquals(flipNotAllowed.getDetail(), "Only closed or marked cells can be flipped");

        Assertions.assertEquals(flagNotAllowed.getType(), "ACTION_NOT_ALLOWED");
        Assertions.assertEquals(flagNotAllowed.getStatus(), 406);
        Assertions.assertEquals(flagNotAllowed.getDetail(), "Only closed or marked cells can be flagged");

        Assertions.assertEquals(markNotAllowed.getType(), "ACTION_NOT_ALLOWED");
        Assertions.assertEquals(markNotAllowed.getStatus(), 406);
        Assertions.assertEquals(markNotAllowed.getDetail(), "Only closed or flagged cells can be marked");

        Assertions.assertEquals(removeTagsAllowed.getType(), "ACTION_NOT_ALLOWED");
        Assertions.assertEquals(removeTagsAllowed.getStatus(), 406);
        Assertions.assertEquals(removeTagsAllowed.getDetail(), "Only marked or flagged cells can be unmarked");

    }

    private void assertCellStatus(CellState cellState, List<Cell> cells) {
        for (Cell cell : cells) {
            Assertions.assertEquals(cellState, cell.getState());
        }
    }

}
