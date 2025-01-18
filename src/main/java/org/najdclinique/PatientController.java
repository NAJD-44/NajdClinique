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
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private ComboBox<Genre> genreComboBox;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numTelField;
    @FXML
    private ImageView photoProfilView;

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> idColumn;
    @FXML
    private TableColumn<Patient, String> nomColumn;
    @FXML
    private TableColumn<Patient, String> prenomColumn;
    @FXML
    private TableColumn<Patient, Genre> genreColumn;
    @FXML
    private TableColumn<Patient, Date> dateNaissanceColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> numTelColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private String photoProfilPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Genre> genres = FXCollections.observableArrayList(Genre.values());
            genreComboBox.setItems(genres);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            numTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));


            loadPatients();


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
            patientTable.setItems(patientList);
            return;
        }

        ObservableList<Patient> filteredList = FXCollections.observableArrayList();
        for (Patient patient : patientList) {
            if (patient.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                    patient.getPrenom().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(patient);
            }
        }
        patientTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(patientTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("ID,Nom,Prénom,Genre,Date de Naissance,Email,Numéro de Téléphone");

                for (Patient patient : patientTable.getItems()) {
                    writer.println(
                            patient.getId() + "," +
                                    patient.getNom() + "," +
                                    patient.getPrenom() + "," +
                                    patient.getGenre() + "," +
                                    patient.getDateNaissance() + "," +
                                    patient.getEmail() + "," +
                                    patient.getNumTel()
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

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(getAllPatients());
        patientTable.setItems(patientList);
    }

    @FXML
    private void handleAddPatient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        Genre genre = genreComboBox.getValue();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String email = emailField.getText();
        String numTel = numTelField.getText();
        String photoProfil = photoProfilPath;

        if (nom.isEmpty() || prenom.isEmpty() || genre == null || dateNaissance == null || email.isEmpty() || numTel.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        if (!validateEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }
        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setGenre(genre);
        patient.setDateNaissance(Date.valueOf(dateNaissance));
        patient.setEmail(email);
        patient.setNumTel(numTel);
        patient.setPhotoProfil(photoProfil);

        addPatient(patient);
        loadPatients();
        clearFields();
    }

    @FXML
    private void handleUpdatePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient à modifier.");
            return;
        }

        selectedPatient.setNom(nomField.getText());
        selectedPatient.setPrenom(prenomField.getText());
        selectedPatient.setGenre(genreComboBox.getValue());
        selectedPatient.setDateNaissance(Date.valueOf(dateNaissancePicker.getValue()));
        selectedPatient.setEmail(emailField.getText());
        selectedPatient.setNumTel(numTelField.getText());
        selectedPatient.setPhotoProfil(photoProfilPath);

        updatePatient(selectedPatient);
        loadPatients();
        clearFields();
    }

    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient à supprimer.");
            return;
        }

        deletePatient(selectedPatient.getId());
        loadPatients();
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
        genreComboBox.getSelectionModel().clearSelection();
        dateNaissancePicker.setValue(null);
        emailField.clear();
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


    private void addPatient(Patient patient) {
        String insertQuery = "INSERT INTO patient (nom, prenom, genre, photo_profil, date_naissance, email, num_tel) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setString(3, patient.getGenre().name());
            stmt.setString(4, patient.getPhotoProfil());
            stmt.setDate(5, new java.sql.Date(patient.getDateNaissance().getTime()));
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getNumTel());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePatient(Patient patient) {
        String updateQuery = "UPDATE patient SET nom = ?, prenom = ?, genre = ?, photo_profil = ?, " +
                "date_naissance = ?, email = ?, num_tel = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setString(3, patient.getGenre().name());
            stmt.setString(4, patient.getPhotoProfil());
            stmt.setDate(5, new java.sql.Date(patient.getDateNaissance().getTime()));
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getNumTel());
            stmt.setInt(8, patient.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePatient(int id) {
        String deleteQuery = "DELETE FROM patient WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM patient";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(selectAllQuery)) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        Genre.valueOf(resultSet.getString("genre")),
                        resultSet.getString("photo_profil"),
                        resultSet.getDate("date_naissance"),
                        resultSet.getString("email"),
                        resultSet.getString("num_tel")
                );
                patients.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }
}