package org.najdclinique;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.PieChart;


public class DashboardController implements Initializable {

    @FXML
    private Label patientsCountLabel;
    @FXML
    private Label medecinsCountLabel;
    @FXML
    private Label infirmiersCountLabel;
    @FXML
    private Label facturesCountLabel;
    @FXML
    private Label rendezVousCountLabel;
    @FXML
    private Label dossiersCountLabel;
    @FXML
    private PieChart patientsGenreChart;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadStatistics();
        loadPatientsGenreChart();
    }


    private void loadStatistics() {

        int patientsCount = PatientDAO.getAllPatients().size();
        int medecinsCount = MedecinDAO.getAllMedecins().size();
        int infirmiersCount = InfirmierDAO.getAllInfirmiers().size();
        int facturesCount = FactureDAO.getAllFactures().size();
        int rendezVousCount = RendezVousDAO.getAllRendezVous().size();
        int dossiersCount = DossierMedicalDAO.getAllDossierMedicals().size();


        patientsCountLabel.setText(String.valueOf(patientsCount));
        medecinsCountLabel.setText(String.valueOf(medecinsCount));
        infirmiersCountLabel.setText(String.valueOf(infirmiersCount));
        facturesCountLabel.setText(String.valueOf(facturesCount));
        rendezVousCountLabel.setText(String.valueOf(rendezVousCount));
        dossiersCountLabel.setText(String.valueOf(dossiersCount));
    }

    private void loadPatientsGenreChart() {
        int maleCount = PatientDAO.getPatientsByGenre(Genre.MASCULIN).size();
        int femaleCount = PatientDAO.getPatientsByGenre(Genre.FEMININ).size();

        PieChart.Data maleData = new PieChart.Data("Hommes", maleCount);
        PieChart.Data femaleData = new PieChart.Data("Femmes", femaleCount);

        patientsGenreChart.getData().addAll(maleData, femaleData);
    }
}