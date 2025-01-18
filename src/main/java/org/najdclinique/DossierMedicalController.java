package org.najdclinique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DossierMedicalController implements Initializable {

    @FXML
    private ComboBox<Patient> patientComboBox;
    @FXML
    private DatePicker dateCreationPicker;
    @FXML
    private DatePicker dateMiseAJourPicker;
    @FXML
    private TextArea detailsField;

    @FXML
    private TableView<DossierMedical> dossierMedicalTable;
    @FXML
    private TableColumn<DossierMedical, Integer> idColumn;
    @FXML
    private TableColumn<DossierMedical, String> patientColumn;
    @FXML
    private TableColumn<DossierMedical, LocalDate> dateCreationColumn;
    @FXML
    private TableColumn<DossierMedical, LocalDate> dateMiseAJourColumn;
    @FXML
    private TableColumn<DossierMedical, String> detailsColumn;

    @FXML
    private TextField searchField;

    private ObservableList<DossierMedical> dossierMedicalList = FXCollections.observableArrayList();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            patientList.addAll(PatientDAO.getAllPatientsForDisplay());
            patientComboBox.setItems(patientList);


            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
            dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
            dateMiseAJourColumn.setCellValueFactory(new PropertyValueFactory<>("dateMiseAJour"));
            detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));


            loadDossierMedicals();


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
            dossierMedicalTable.setItems(dossierMedicalList);
            return;
        }

        ObservableList<DossierMedical> filteredList = FXCollections.observableArrayList();
        for (DossierMedical dossier : dossierMedicalList) {
            if (dossier.getPatient().toString().toLowerCase().contains(keyword.toLowerCase()) ||
                    dossier.getDetails().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(dossier);
            }
        }
        dossierMedicalTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(dossierMedicalTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("ID,Patient,Date de Création,Date de Mise à Jour,Détails");


                for (DossierMedical dossier : dossierMedicalTable.getItems()) {
                    writer.println(
                            dossier.getId() + "," +
                                    dossier.getPatient().toString() + "," +
                                    dossier.getDateCreation() + "," +
                                    dossier.getDateMiseAJour() + "," +
                                    dossier.getDetails()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private void loadDossierMedicals() {
        dossierMedicalList.clear();
        dossierMedicalList.addAll(DossierMedicalDAO.getAllDossierMedicals());
        dossierMedicalTable.setItems(dossierMedicalList);
    }


    @FXML
    private void handleAddDossierMedical() {
        Patient patient = patientComboBox.getValue();
        LocalDate dateCreation = dateCreationPicker.getValue();
        LocalDate dateMiseAJour = dateMiseAJourPicker.getValue();
        String details = detailsField.getText();

        if (patient == null || dateCreation == null || details.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        DossierMedical dossier = new DossierMedical();
        dossier.setPatient(patient);
        dossier.setDateCreation(dateCreation);
        dossier.setDateMiseAJour(dateMiseAJour);
        dossier.setDetails(details);

        DossierMedicalDAO.addDossierMedical(dossier);
        loadDossierMedicals();
        clearFields();
    }


    @FXML
    private void handleUpdateDossierMedical() {
        DossierMedical selectedDossier = dossierMedicalTable.getSelectionModel().getSelectedItem();
        if (selectedDossier == null) {
            showAlert("Erreur", "Veuillez sélectionner un dossier médical à modifier.");
            return;
        }

        selectedDossier.setPatient(patientComboBox.getValue());
        selectedDossier.setDateCreation(dateCreationPicker.getValue());
        selectedDossier.setDateMiseAJour(dateMiseAJourPicker.getValue());
        selectedDossier.setDetails(detailsField.getText());

        DossierMedicalDAO.updateDossierMedical(selectedDossier);
        loadDossierMedicals();
        clearFields();
    }


    @FXML
    private void handleDeleteDossierMedical() {
        DossierMedical selectedDossier = dossierMedicalTable.getSelectionModel().getSelectedItem();
        if (selectedDossier == null) {
            showAlert("Erreur", "Veuillez sélectionner un dossier médical à supprimer.");
            return;
        }

        DossierMedicalDAO.deleteDossierMedical(selectedDossier.getId());
        loadDossierMedicals();
        clearFields();
    }


    private void clearFields() {
        patientComboBox.getSelectionModel().clearSelection();
        dateCreationPicker.setValue(null);
        dateMiseAJourPicker.setValue(null);
        detailsField.clear();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}