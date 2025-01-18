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

public class ChambreController implements Initializable {

    @FXML
    private TextField numeroField;
    @FXML
    private TextField capaciteField;
    @FXML
    private ComboBox<Etat> etatComboBox;
    @FXML
    private DatePicker dateOccupationPicker;
    @FXML
    private DatePicker dateLiberationPicker;

    @FXML
    private TableView<Chambre> chambreTable;
    @FXML
    private TableColumn<Chambre, Integer> idColumn;
    @FXML
    private TableColumn<Chambre, Integer> numeroColumn;
    @FXML
    private TableColumn<Chambre, Integer> capaciteColumn;
    @FXML
    private TableColumn<Chambre, Etat> etatColumn;
    @FXML
    private TableColumn<Chambre, LocalDate> dateOccupationColumn;
    @FXML
    private TableColumn<Chambre, LocalDate> dateLiberationColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Chambre> chambreList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            ObservableList<Etat> etats = FXCollections.observableArrayList(Etat.values());
            etatComboBox.setItems(etats);


            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
            capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));
            etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
            dateOccupationColumn.setCellValueFactory(new PropertyValueFactory<>("dateOccupation"));
            dateLiberationColumn.setCellValueFactory(new PropertyValueFactory<>("dateLiberation"));

            loadChambres();


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
            chambreTable.setItems(chambreList);
            return;
        }

        ObservableList<Chambre> filteredList = FXCollections.observableArrayList();
        for (Chambre chambre : chambreList) {
            if (String.valueOf(chambre.getNumero()).contains(keyword) ||
                    chambre.getEtat().name().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(chambre);
            }
        }
        chambreTable.setItems(filteredList);
    }


    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(chambreTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("ID,Numéro,Capacité,État,Date d'Occupation,Date de Libération");


                for (Chambre chambre : chambreTable.getItems()) {
                    writer.println(
                            chambre.getId() + "," +
                                    chambre.getNumero() + "," +
                                    chambre.getCapacite() + "," +
                                    chambre.getEtat() + "," +
                                    chambre.getDateOccupation() + "," +
                                    chambre.getDateLiberation()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private void loadChambres() {
        chambreList.clear();
        chambreList.addAll(ChambreDAO.getAllChambres());
        chambreTable.setItems(chambreList);
    }


    @FXML
    private void handleAddChambre() {
        int numero = Integer.parseInt(numeroField.getText());
        int capacite = Integer.parseInt(capaciteField.getText());
        Etat etat = etatComboBox.getValue();
        LocalDate dateOccupation = dateOccupationPicker.getValue();
        LocalDate dateLiberation = dateLiberationPicker.getValue();

        if (numeroField.getText().isEmpty() || capaciteField.getText().isEmpty() || etat == null || dateOccupation == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        Chambre chambre = new Chambre();
        chambre.setNumero(numero);
        chambre.setCapacite(capacite);
        chambre.setEtat(etat);
        chambre.setDateOccupation(dateOccupation);
        chambre.setDateLiberation(dateLiberation);

        ChambreDAO.addChambre(chambre);
        loadChambres();
        clearFields();
    }


    @FXML
    private void handleUpdateChambre() {
        Chambre selectedChambre = chambreTable.getSelectionModel().getSelectedItem();
        if (selectedChambre == null) {
            showAlert("Erreur", "Veuillez sélectionner une chambre à modifier.");
            return;
        }

        selectedChambre.setNumero(Integer.parseInt(numeroField.getText()));
        selectedChambre.setCapacite(Integer.parseInt(capaciteField.getText()));
        selectedChambre.setEtat(etatComboBox.getValue());
        selectedChambre.setDateOccupation(dateOccupationPicker.getValue());
        selectedChambre.setDateLiberation(dateLiberationPicker.getValue());

        ChambreDAO.updateChambre(selectedChambre);
        loadChambres();
        clearFields();
    }


    @FXML
    private void handleDeleteChambre() {
        Chambre selectedChambre = chambreTable.getSelectionModel().getSelectedItem();
        if (selectedChambre == null) {
            showAlert("Erreur", "Veuillez sélectionner une chambre à supprimer.");
            return;
        }

        ChambreDAO.deleteChambre(selectedChambre.getId());
        loadChambres();
        clearFields();
    }


    private void clearFields() {
        numeroField.clear();
        capaciteField.clear();
        etatComboBox.getSelectionModel().clearSelection();
        dateOccupationPicker.setValue(null);
        dateLiberationPicker.setValue(null);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}