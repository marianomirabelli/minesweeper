package com.deviget.minesweeper.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Set;

@Configuration
public class ConvertersConfiguration {

    @Autowired
    private Set<Converter> customConverters;

    @Bean
    public DefaultConversionService defaultConversionService(Set<Converter> customConverters){
       DefaultConversionService defaultConversionService = new DefaultConversionService();
       for(Converter converter:customConverters){
           defaultConversionService.addConverter(converter);
       }

       return defaultConversionService;
    }
}
