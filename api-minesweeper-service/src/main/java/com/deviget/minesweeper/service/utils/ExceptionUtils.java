package com.deviget.minesweeper.service.utils;

import com.deviget.minesweeper.service.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
public class ExceptionUtils {

    private final Environment environment;

    @Autowired
    public ExceptionUtils(Environment environment){
        this.environment = environment;
    }


    public GameException buildException(String typeKey, String descriptionKey,String statusKey){
        String type = environment.getProperty(typeKey);
        String description = environment.getProperty(descriptionKey);
        int status = Integer.parseInt(environment.getProperty(statusKey));
        return new GameException(type,description,status);
    }

}
