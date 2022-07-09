package com.github.vladyslavbabenko.passwordkeeper.javafx;

import com.github.vladyslavbabenko.passwordkeeper.PasswordKeeperApplication;
import com.github.vladyslavbabenko.passwordkeeper.javafx.controller.FXController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder().sources(PasswordKeeperApplication.class).run(args);
    }

    @Override
    public void start(Stage stage) {
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}