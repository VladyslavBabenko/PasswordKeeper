package com.github.vladyslavbabenko.passwordkeeper.javafx.controller;

import com.github.vladyslavbabenko.passwordkeeper.entity.PasswordEntity;
import com.github.vladyslavbabenko.passwordkeeper.javafx.service.PasswordKeeperService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("GUI.fxml")
public class FXController implements Initializable {
    private final PasswordKeeperService passwordService;

    @FXML
    private TableView<PasswordEntity> tableView;
    @FXML
    private TableColumn<PasswordEntity, String> tableViewID;
    @FXML
    private TableColumn<PasswordEntity, String> tableViewTitle;
    @FXML
    private TableColumn<PasswordEntity, String> tableViewPassword;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField deleteIdInput;
    @FXML
    private Slider randomPasswordLengthSlider;

    @Autowired
    public FXController(PasswordKeeperService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewID.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        tableViewTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        tableViewPassword.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());

        tableView.setItems(passwordService.getPasswords());
        tableView.setEditable(true);
        tableViewID.setEditable(false);
        tableViewTitle.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tableViewPassword.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    }

    /**
     * Updates the old title and saves the new title from the TableView to the entry
     *
     * @param editedCell updated title from TableView
     */
    public void updateTitle(TableColumn.CellEditEvent<PasswordEntity, String> editedCell) {
        PasswordEntity selectedEntity = tableView.getSelectionModel().getSelectedItem();
        selectedEntity.setTitle(editedCell.getNewValue());
        passwordService.editEntry(selectedEntity);
        passwordService.saveEntry();
    }

    /**
     * Updates the old password and saves the new password from the TableView to the entry
     *
     * @param editedCell updated password from TableView
     */
    public void updatePassword(TableColumn.CellEditEvent<PasswordEntity, String> editedCell) {
        PasswordEntity selectedEntity = tableView.getSelectionModel().getSelectedItem();
        selectedEntity.setPassword(editedCell.getNewValue());
        passwordService.editEntry(selectedEntity);
        passwordService.saveEntry();
    }

    /**
     * Creates a new entry if input fields are not empty
     */
    public void createEntry() {
        if (titleInput.getText() != null
                && passwordInput.getText() != null
                && !titleInput.getText().isEmpty()
                && !passwordInput.getText().isEmpty()) {
            passwordService.addEntry(new PasswordEntity(passwordService.getEntryId(), titleInput.getText(), passwordInput.getText()));
        }
        passwordService.saveEntry();
    }

    /**
     * Deletes the entry if it exists
     */
    public void deleteEntry() {
        int id = Integer.parseInt(deleteIdInput.getText());
        if (id > 0) {
            passwordService.deleteEntry(id);
            passwordService.saveEntry();
        }
    }

    /**
     * Generates a random string value, the length of which depends on the value of the slider
     */
    public void getRandomPassword() {
        passwordInput.setText(passwordService.generatePassword((int) randomPasswordLengthSlider.getValue()));
    }
}