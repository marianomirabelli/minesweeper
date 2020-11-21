package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.ApiErrorDTO;
import com.deviget.minesweeper.service.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.sql.rowset.WebRowSet;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GameControllerAdvisor {

    private Map<Integer, HttpStatus> statusCodeMap;

    @Autowired
    public GameControllerAdvisor(){
        this.statusCodeMap =Arrays.stream(HttpStatus.values())
                            .collect(Collectors.toMap(HttpStatus::value,
                                     Function.identity(),
                                     (s1,s2)->s1==HttpStatus.FOUND?s1:s2));
    }

    @ExceptionHandler(GameException.class)
    public ResponseEntity<ApiErrorDTO> handleGameException(GameException ex, WebRowSet request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(ex.getType(), ex.getDetail(), ex.getStatus());
        return new ResponseEntity<>(apiErrorDTO,this.statusCodeMap.get(apiErrorDTO.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericExceptions(Exception ex,WebRowSet request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO("COMMON-UNEXPECTED-ERROR", ex.getMessage(), 500);
        return new ResponseEntity<>(apiErrorDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
