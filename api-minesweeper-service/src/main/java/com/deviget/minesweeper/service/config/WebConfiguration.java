package com.deviget.minesweeper.service.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.info.MapInfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class WebConfiguration {

    private final static String APP_INFO = "app-info.yml";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2CborHttpMessageConverter(ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public InfoEndpoint infoEndpoint() {
        Yaml yaml = new Yaml();
        Map<String, Object> appInfo = (Map<String, Object>) yaml
                .loadAs(this.getClass().getClassLoader().getResourceAsStream(APP_INFO), Map.class);
        InfoContributor infoContributor = new MapInfoContributor(appInfo);
        return new InfoEndpoint(Arrays.asList(infoContributor));
    }
}
