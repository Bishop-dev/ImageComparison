package com.hubachov.app;

import com.hubachov.web.ImagesController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Object[] classes = {Application.class, ImagesController.class, AppConfig.class};
        SpringApplication.run(classes, args);
    }
}
