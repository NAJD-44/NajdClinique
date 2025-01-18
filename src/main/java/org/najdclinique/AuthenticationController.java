package org.najdclinique;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> ComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            ComboBox.setItems(FXCollections.observableArrayList("Administrateur", "Infirmier", "Medecin"));
    }

    @FXML
    private void handleResetButtonAction() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleLoginButtonAction() throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = ComboBox.getValue();
        if (username.isEmpty() || password.isEmpty() || selectedRole.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }
        switch (selectedRole) {
            case "Administrateur":
                validateAdministrateur(username, password);
                break;
            case "Infirmier":
                validateInfirmier(username, password);
                break;
            case "Medecin":
                validateMedecin(username, password);
                break;
            default:
                showAlert("Error", "Invalid role selected.");
                break;
        }
    }

    private void validateAdministrateur(String username, String password) throws SQLException {
        DatabaseUtil conn = new DatabaseUtil();
        Connection connection = conn.getConnection();
        String verifyLogin = "SELECT count(1) FROM administrateur WHERE username='" + username + "' AND password='" + password + "'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet query = stmt.executeQuery(verifyLogin);
            while (query.next()) {
                if (query.getInt(1) == 1) {
                    navigateToDashboard(username);
                } else {
                    showAlert("Error", "Invalid Username or Password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateInfirmier(String email, String password) throws SQLException {
        DatabaseUtil conn = new DatabaseUtil();
        Connection connection = conn.getConnection();
        String verifyLogin = "SELECT count(1) FROM infirmier WHERE email='" + email + "' AND password='" + password + "'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet query = stmt.executeQuery(verifyLogin);
            while (query.next()) {
                if (query.getInt(1) == 1) {
                    navigateToDashboard(email);
                } else {
                    showAlert("Error", "Invalid Email or Password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    private void validateMedecin(String email, String password) throws SQLException {
        DatabaseUtil conn = new DatabaseUtil();
        Connection connection = conn.getConnection();
        String verifyLogin = "SELECT count(1) FROM medecin WHERE email='" + email + "' AND password='" + password + "'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet query = stmt.executeQuery(verifyLogin);
            while (query.next()) {
                if (query.getInt(1) == 1) {
                    navigateToDashboard(email);
                } else {
                    showAlert("Error", "Invalid Email or Password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/najdclinique/AdminPanelView.fxml"));
            Parent root = loader.load();

            AdminPanelController adminPanelController = loader.getController();
            adminPanelController.setWelcomeMessage(username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("NajdClinique");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the dashboard.");
        }
    }
}