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
import java.sql.Date;
import java.util.ResourceBundle;

public class MedecinController implements Initializable {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private ComboBox<Status> statusComboBox;
    @FXML
    private TextField specialiteField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numTelField;
    @FXML
    private ImageView photoProfilView;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TableView<Medecin> medecinTable;
    @FXML
    private TableColumn<Medecin, Integer> idColumn;
    @FXML
    private TableColumn<Medecin, String> nomColumn;
    @FXML
    private TableColumn<Medecin, String> prenomColumn;
    @FXML
    private TableColumn<Medecin, Status> statusColumn;
    @FXML
    private TableColumn<Medecin, String> specialiteColumn;
    @FXML
    private TableColumn<Medecin, String> emailColumn;
    @FXML
    private TableColumn<Medecin, String> numTelColumn;
    @FXML
    private TableColumn<Medecin, String> passwordColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Medecin> medecinList = FXCollections.observableArrayList();
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
            specialiteColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            numTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
            loadMedecins();

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
            medecinTable.setItems(medecinList);
            return;
        }

        ObservableList<Medecin> filteredList = FXCollections.observableArrayList();
        for (Medecin medecin : medecinList) {
            if (medecin.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                    medecin.getPrenom().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(medecin);
            }
        }
        medecinTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(medecinTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Nom,Prénom,Status,Spécialité,Email,Numéro de Téléphone");

                for (Medecin medecin : medecinTable.getItems()) {
                    writer.println(
                            medecin.getId() + "," +
                                    medecin.getNom() + "," +
                                    medecin.getPrenom() + "," +
                                    medecin.getStatus() + "," +
                                    medecin.getSpecialite() + "," +
                                    medecin.getEmail() + "," +
                                    medecin.getNumTel()
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

    private void loadMedecins() {
        medecinList.clear();
        medecinList.addAll(MedecinDAO.getAllMedecins());
        medecinTable.setItems(medecinList);
    }

    @FXML
    private void handleAddMedecin() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        Status status = statusComboBox.getValue();
        String specialite = specialiteField.getText();
        String email = emailField.getText();
        String numTel = numTelField.getText();
        String photoProfil = photoProfilPath;
        String password = passwordField.getText();
        if (nom.isEmpty() || prenom.isEmpty() || status == null || specialite.isEmpty() || email.isEmpty() || numTel.isEmpty()|| password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if (!validateEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        Medecin medecin = new Medecin();
        medecin.setNom(nom);
        medecin.setPrenom(prenom);
        medecin.setStatus(status);
        medecin.setSpecialite(specialite);
        medecin.setEmail(email);
        medecin.setNumTel(numTel);
        medecin.setPhotoProfil(photoProfil);
        medecin.setPassword(password);
        MedecinDAO.addMedecin(medecin);
        loadMedecins();
        clearFields();
    }

    @FXML
    private void handleUpdateMedecin() {
        Medecin selectedMedecin = medecinTable.getSelectionModel().getSelectedItem();
        if (selectedMedecin == null) {
            showAlert("Erreur", "Veuillez sélectionner un médecin à modifier.");
            return;
        }

        selectedMedecin.setNom(nomField.getText());
        selectedMedecin.setPrenom(prenomField.getText());
        selectedMedecin.setStatus(statusComboBox.getValue());
        selectedMedecin.setSpecialite(specialiteField.getText());
        selectedMedecin.setEmail(emailField.getText());
        selectedMedecin.setNumTel(numTelField.getText());
        selectedMedecin.setPhotoProfil(photoProfilPath);
        selectedMedecin.setPassword(passwordField.getText());
        MedecinDAO.updateMedecin(selectedMedecin);
        loadMedecins();
        clearFields();
    }

    @FXML
    private void handleDeleteMedecin() {
        Medecin selectedMedecin = medecinTable.getSelectionModel().getSelectedItem();
        if (selectedMedecin == null) {
            showAlert("Erreur", "Veuillez sélectionner un médecin à supprimer.");
            return;
        }

        MedecinDAO.deleteMedecin(selectedMedecin.getId());
        loadMedecins();
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
        specialiteField.clear();
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