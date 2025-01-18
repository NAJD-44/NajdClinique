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
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class RendezVousController implements Initializable {

    @FXML
    private DatePicker dateHourPicker;
    @FXML
    private ComboBox<Patient> patientComboBox;
    @FXML
    private ComboBox<Medecin> medecinComboBox;

    @FXML
    private TableView<RendezVous> rendezVousTable;
    @FXML
    private TableColumn<RendezVous, Integer> idColumn;
    @FXML
    private TableColumn<RendezVous, Date> dateHourColumn;
    @FXML
    private TableColumn<RendezVous, String> patientColumn;
    @FXML
    private TableColumn<RendezVous, String> medecinColumn;

    @FXML
    private TextField searchField;

    private ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private ObservableList<Medecin> medecinList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            patientList.addAll(PatientDAO.getAllPatientsForDisplay());
            patientComboBox.setItems(patientList);

            medecinList.addAll(MedecinDAO.getAllMedecinsForDisplay());
            medecinComboBox.setItems(medecinList);


            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            dateHourColumn.setCellValueFactory(new PropertyValueFactory<>("dateHour"));
            patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
            medecinColumn.setCellValueFactory(new PropertyValueFactory<>("medecin"));


            loadRendezVous();


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
            rendezVousTable.setItems(rendezVousList);
            return;
        }

        ObservableList<RendezVous> filteredList = FXCollections.observableArrayList();
        for (RendezVous rendezVous : rendezVousList) {
            if (rendezVous.getPatient().toString().toLowerCase().contains(keyword.toLowerCase()) ||
                    rendezVous.getMedecin().toString().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(rendezVous);
            }
        }
        rendezVousTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(rendezVousTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("ID,Date et Heure,Patient,Médecin");

                for (RendezVous rendezVous : rendezVousTable.getItems()) {
                    writer.println(
                            rendezVous.getId() + "," +
                                    rendezVous.getDateHour() + "," +
                                    rendezVous.getPatient().toString() + "," +
                                    rendezVous.getMedecin().toString()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private void loadRendezVous() {
        rendezVousList.clear();
        rendezVousList.addAll(RendezVousDAO.getAllRendezVous());
        rendezVousTable.setItems(rendezVousList);
    }


    @FXML
    private void handleAddRendezVous() {
        Date dateHour = Timestamp.valueOf(dateHourPicker.getValue().atStartOfDay());
        Patient patient = patientComboBox.getValue();
        Medecin medecin = medecinComboBox.getValue();

        if (dateHour == null || patient == null || medecin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        RendezVous rendezVous = new RendezVous();
        rendezVous.setDateHour(dateHour);
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);

        RendezVousDAO.addRendezVous(rendezVous);
        loadRendezVous();
        clearFields();
    }

    @FXML
    private void handleUpdateRendezVous() {
        RendezVous selectedRendezVous = rendezVousTable.getSelectionModel().getSelectedItem();
        if (selectedRendezVous == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à modifier.");
            return;
        }

        selectedRendezVous.setDateHour(Timestamp.valueOf(dateHourPicker.getValue().atStartOfDay()));
        selectedRendezVous.setPatient(patientComboBox.getValue());
        selectedRendezVous.setMedecin(medecinComboBox.getValue());

        RendezVousDAO.updateRendezVous(selectedRendezVous);
        loadRendezVous();
        clearFields();
    }

    @FXML
    private void handleDeleteRendezVous() {
        RendezVous selectedRendezVous = rendezVousTable.getSelectionModel().getSelectedItem();
        if (selectedRendezVous == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à supprimer.");
            return;
        }

        RendezVousDAO.deleteRendezVous(selectedRendezVous.getId());
        loadRendezVous();
        clearFields();
    }

    private void clearFields() {
        dateHourPicker.setValue(null);
        patientComboBox.getSelectionModel().clearSelection();
        medecinComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}