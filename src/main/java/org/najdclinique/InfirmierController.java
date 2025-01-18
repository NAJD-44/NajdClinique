package org.najdclinique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class InfirmierController implements Initializable {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private ComboBox<Status> statusComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numTelField;
    @FXML
    private ImageView photoProfilView;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TableView<Infirmier> infirmierTable;
    @FXML
    private TableColumn<Infirmier, Integer> idColumn;
    @FXML
    private TableColumn<Infirmier, String> nomColumn;
    @FXML
    private TableColumn<Infirmier, String> prenomColumn;
    @FXML
    private TableColumn<Infirmier, Status> statusColumn;
    @FXML
    private TableColumn<Infirmier, String> emailColumn;
    @FXML
    private TableColumn<Infirmier, String> numTelColumn;
    @FXML
    private TableColumn<Infirmier, String> passwordColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Infirmier> infirmierList = FXCollections.observableArrayList();
    private String photoProfilPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Status> statuses = FXCollections.observableArrayList(Status.values());
            statusComboBox.setItems(statuses);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            numTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            loadInfirmiers();


            setupSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
    }


    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            infirmierTable.setItems(infirmierList);
            return;
        }

        ObservableList<Infirmier> filteredList = FXCollections.observableArrayList();
        for (Infirmier infirmier : infirmierList) {
            if (infirmier.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                    infirmier.getPrenom().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(infirmier);
            }
        }
        infirmierTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(infirmierTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Nom,Prénom,Status,Email,Numéro de Téléphone");

                for (Infirmier infirmier : infirmierTable.getItems()) {
                    writer.println(
                            infirmier.getId() + "," +
                                    infirmier.getNom() + "," +
                                    infirmier.getPrenom() + "," +
                                    infirmier.getStatus() + "," +
                                    infirmier.getEmail() + "," +
                                    infirmier.getNumTel()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    private void loadInfirmiers() {
        infirmierList.clear();
        infirmierList.addAll(InfirmierDAO.getAllInfirmiers());
        infirmierTable.setItems(infirmierList);
    }

    @FXML
    private void handleAddInfirmier() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        Status status = statusComboBox.getValue();
        String email = emailField.getText();
        String numTel = numTelField.getText();
        String photoProfil = photoProfilPath;
        String password = passwordField.getText();
        if (nom.isEmpty() || prenom.isEmpty() || status == null || email.isEmpty() || numTel.isEmpty()|| password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if (!validateEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        Infirmier infirmier = new Infirmier();
        infirmier.setNom(nom);
        infirmier.setPrenom(prenom);
        infirmier.setStatus(status);
        infirmier.setEmail(email);
        infirmier.setNumTel(numTel);
        infirmier.setPhotoProfil(photoProfil);
        infirmier.setPassword(password);
        InfirmierDAO.addInfirmier(infirmier);
        loadInfirmiers();
        clearFields();
    }


    @FXML
    private void handleUpdateInfirmier() {
        Infirmier selectedInfirmier = infirmierTable.getSelectionModel().getSelectedItem();
        if (selectedInfirmier == null) {
            showAlert("Erreur", "Veuillez sélectionner un infirmier à modifier.");
            return;
        }

        selectedInfirmier.setNom(nomField.getText());
        selectedInfirmier.setPrenom(prenomField.getText());
        selectedInfirmier.setStatus(statusComboBox.getValue());
        selectedInfirmier.setEmail(emailField.getText());
        selectedInfirmier.setNumTel(numTelField.getText());
        selectedInfirmier.setPhotoProfil(photoProfilPath);
        selectedInfirmier.setPassword(passwordField.getText());
        InfirmierDAO.updateInfirmier(selectedInfirmier);
        loadInfirmiers();
        clearFields();
    }

    @FXML
    private void handleDeleteInfirmier() {
        Infirmier selectedInfirmier = infirmierTable.getSelectionModel().getSelectedItem();
        if (selectedInfirmier == null) {
            showAlert("Erreur", "Veuillez sélectionner un infirmier à supprimer.");
            return;
        }

        InfirmierDAO.deleteInfirmier(selectedInfirmier.getId());
        loadInfirmiers();
        clearFields();
    }


    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(photoProfilView.getScene().getWindow());
        if (selectedFile != null) {

            Image image = new Image(selectedFile.toURI().toString());
            photoProfilView.setImage(image);


            photoProfilPath = selectedFile.getAbsolutePath();
        }
    }


    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        emailField.clear();
        passwordField.clear();
        numTelField.clear();
        photoProfilView.setImage(null);
        photoProfilPath = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}