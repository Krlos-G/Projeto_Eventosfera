package com.apievento.app.Event_Image;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class EventImageConfiguration {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Bean
    public String uploadDirectory(){
        return uploadDir;
    }

    @PostConstruct
    public void init(){
        try{
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }
}
