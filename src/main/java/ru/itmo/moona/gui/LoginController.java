package ru.itmo.moona.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import ru.itmo.moona.app.JavaFXApp;
import ru.itmo.moona.database.UserRepository;
import ru.itmo.moona.domain.users.CurrentUser;
import ru.itmo.moona.domain.users.User;

import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private UserRepository repository;
    private JavaFXApp app;

    public void setApp(JavaFXApp app) {
        this.app = app;
    }

    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    private void showError(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error!");
        alert.setContentText(e);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


    private void showSuccess(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("success!");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


    @FXML
    private void handleLogin() throws SQLException {
        try {
            String login = username.getText();
            String pass = password.getText();
            User user = repository.getUser(login);
            if (repository.authenticate(pass, user)) {
                CurrentUser.getInstance().login(user);
                app.showTable();
            } else {
                showError("wrong password");
            }
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleRegister() throws SQLException {
        try {
            String login = username.getText();
            String pass = password.getText();
            if (login.isBlank() || pass.isBlank()) {
                throw new IllegalArgumentException("login or password can't be blank");
            }
            repository.save(login, pass);
            showSuccess("successfully added user " + login);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
}
