package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.UserDTO;
import com.deviget.minesweeper.service.model.User;
import com.deviget.minesweeper.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
@Api(value = "/users", tags = { "User Controller" })
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @ApiOperation(value = "createANewUser",
                  notes = "Creates a new user. Also this endpoint set the cookie for the user",
                  response = GameDTO.class)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, HttpServletResponse response){
        User user = new User(userDTO.getUserName());
        user = userService.createNewUser(user);
        Cookie cookie = new Cookie("userName",user.getUserName());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        return new ResponseEntity(new UserDTO(user.getId(),user.getUserName()), HttpStatus.CREATED);
    }

}
