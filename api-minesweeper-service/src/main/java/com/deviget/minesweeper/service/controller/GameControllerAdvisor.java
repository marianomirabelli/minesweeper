package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.ApiErrorDTO;
import com.deviget.minesweeper.service.exception.MinesweeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GameControllerAdvisor extends ResponseEntityExceptionHandler {

    private Map<Integer, HttpStatus> statusCodeMap;

    Logger logger = LoggerFactory.getLogger(GameControllerAdvisor.class);

    @Autowired
    public GameControllerAdvisor(){
        this.statusCodeMap =Arrays.stream(HttpStatus.values())
                            .collect(Collectors.toMap(HttpStatus::value,
                                     Function.identity(),
                                     (s1,s2)->s1==HttpStatus.FOUND?s1:s2));
    }

    @ExceptionHandler(MinesweeperException.class)
    public ResponseEntity<ApiErrorDTO> handleGameException(MinesweeperException ex, WebRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(ex.getType(), ex.getDetail(), ex.getStatus());
        return new ResponseEntity<>(apiErrorDTO,this.statusCodeMap.get(apiErrorDTO.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleExceptions(Exception ex, WebRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO("UNEXPECTED_ERROR", ex.getMessage(), 500);
        logger.error("Unexpected error",ex);
        return new ResponseEntity<>(apiErrorDTO,this.statusCodeMap.get(apiErrorDTO.getStatus()));
    }

}
