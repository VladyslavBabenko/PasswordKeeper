package com.github.vladyslavbabenko.passwordkeeper.entity;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEntity implements Serializable {
    private int id;
    private String title, password;

    /**
     * @return SimpleStringProperty for TableView
     */
    public SimpleStringProperty getIdProperty() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    /**
     * @return SimpleStringProperty for TableView
     */
    public SimpleStringProperty getTitleProperty() {
        return new SimpleStringProperty(title);
    }

    /**
     * @return SimpleStringProperty for TableView
     */
    public SimpleStringProperty getPasswordProperty() {
        return new SimpleStringProperty(password);
    }
}