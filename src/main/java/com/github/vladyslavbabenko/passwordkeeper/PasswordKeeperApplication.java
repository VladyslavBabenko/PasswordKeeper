package com.github.vladyslavbabenko.passwordkeeper;

import com.github.vladyslavbabenko.passwordkeeper.javafx.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordKeeperApplication {
    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}