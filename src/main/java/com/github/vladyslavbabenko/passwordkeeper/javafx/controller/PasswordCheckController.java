package com.github.vladyslavbabenko.passwordkeeper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("PasswordCheck.fxml")
public class PasswordCheckController {
    private final ConfigurableApplicationContext applicationContext;
    @FXML
    private TextField userCheckField;
    @FXML
    private Label errorLabel;

    @Autowired
    public PasswordCheckController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Verifies that the username entered by the user matches the username on the computer, if true switches to main stage
     */
    public void switchToMainStage(ActionEvent event) {
        if (userCheckField.getText().equals(System.getenv("USERNAME"))) {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(FXController.class);
            Scene passwordCheck = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setResizable(false);
            window.setScene(passwordCheck);
            window.show();
        } else {
            errorLabel.setText("Invalid user");
            errorLabel.setTextFill(Color.RED);
        }
    }
}