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
import java.util.ResourceBundle;

public class FactureController implements Initializable {

    @FXML
    private TextField montantField;
    @FXML
    private TextArea detailsField;
    @FXML
    private ComboBox<Patient> patientComboBox;

    @FXML
    private TableView<Facture> factureTable;
    @FXML
    private TableColumn<Facture, Integer> idColumn;
    @FXML
    private TableColumn<Facture, Double> montantColumn;
    @FXML
    private TableColumn<Facture, String> detailsColumn;
    @FXML
    private TableColumn<Facture, String> patientColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Facture> factureList = FXCollections.observableArrayList();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            patientList.addAll(PatientDAO.getAllPatientsForDisplay());
            patientComboBox.setItems(patientList);

            patientComboBox.setCellFactory(param -> new ListCell<Patient>() {
                @Override
                protected void updateItem(Patient patient, boolean empty) {
                    super.updateItem(patient, empty);
                    if (empty || patient == null) {
                        setText(null);
                    } else {
                        setText(patient.getNom() + " " + patient.getPrenom());
                    }
                }
            });

            patientComboBox.setButtonCell(new ListCell<Patient>() {
                @Override
                protected void updateItem(Patient patient, boolean empty) {
                    super.updateItem(patient, empty);
                    if (empty || patient == null) {
                        setText(null);
                    } else {
                        setText(patient.getNom() + " " + patient.getPrenom());
                    }
                }
            });


            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
            patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
            loadFactures();


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
            factureTable.setItems(factureList);
            return;
        }

        ObservableList<Facture> filteredList = FXCollections.observableArrayList();
        for (Facture facture : factureList) {
            if (String.valueOf(facture.getMontant()).contains(keyword) ||
                    facture.getDetails().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(facture);
            }
        }
        factureTable.setItems(filteredList);
    }


    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(factureTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("ID,Montant,Détails,Patient");


                for (Facture facture : factureTable.getItems()) {
                    writer.println(
                            facture.getId() + "," +
                                    facture.getMontant() + "," +
                                    facture.getDetails() + "," +
                                    facture.getPatient().getNom() + " " + facture.getPatient().getPrenom()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private void loadFactures() {
        factureList.clear();
        factureList.addAll(FactureDAO.getAllFactures());
        factureTable.setItems(factureList);
    }


    @FXML
    private void handleAddFacture() {
        double montant = Double.parseDouble(montantField.getText());
        String details = detailsField.getText();
        Patient patient = patientComboBox.getValue();

        if (montantField.getText().isEmpty() || details.isEmpty() || patient == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Facture facture = new Facture();
        facture.setMontant(montant);
        facture.setDetails(details);
        facture.setPatient(patient);

        FactureDAO.addFacture(facture);
        loadFactures();
        clearFields();
    }


    @FXML
    private void handleUpdateFacture() {
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            showAlert("Erreur", "Veuillez sélectionner une facture à modifier.");
            return;
        }

        selectedFacture.setMontant(Double.parseDouble(montantField.getText()));
        selectedFacture.setDetails(detailsField.getText());
        selectedFacture.setPatient(patientComboBox.getValue());

        FactureDAO.updateFacture(selectedFacture);
        loadFactures();
        clearFields();
    }


    @FXML
    private void handleDeleteFacture() {
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            showAlert("Erreur", "Veuillez sélectionner une facture à supprimer.");
            return;
        }

        FactureDAO.deleteFacture(selectedFacture.getId());
        loadFactures();
        clearFields();
    }


    private void clearFields() {
        montantField.clear();
        detailsField.clear();
        patientComboBox.getSelectionModel().clearSelection();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}