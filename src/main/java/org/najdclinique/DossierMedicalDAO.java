package org.najdclinique;



import org.najdclinique.DossierMedical;
import org.najdclinique.DatabaseUtil;
import org.najdclinique.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DossierMedicalDAO {


    public static void addDossierMedical(DossierMedical dossierMedical) {
        String query = "INSERT INTO dossier_medical (patient_id, date_creation, date_mise_a_jour, details) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, dossierMedical.getPatient().getId());
            statement.setDate(2, Date.valueOf(dossierMedical.getDateCreation()));
            statement.setDate(3, dossierMedical.getDateMiseAJour() != null ? Date.valueOf(dossierMedical.getDateMiseAJour()) : null);
            statement.setString(4, dossierMedical.getDetails());
            statement.executeUpdate();


            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dossierMedical.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public DossierMedical getDossierMedicalById(int id) {
        String query = "SELECT * FROM dossier_medical WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));

                return new DossierMedical(
                        resultSet.getInt("id"),
                        patient,
                        resultSet.getDate("date_creation").toLocalDate(),
                        resultSet.getDate("date_mise_a_jour") != null ? resultSet.getDate("date_mise_a_jour").toLocalDate() : null,
                        resultSet.getString("details")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<DossierMedical> getAllDossierMedicals() {
        List<DossierMedical> dossiers = new ArrayList<>();
        String query = "SELECT * FROM dossier_medical";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));

                DossierMedical dossier = new DossierMedical(
                        resultSet.getInt("id"),
                        patient,
                        resultSet.getDate("date_creation").toLocalDate(),
                        resultSet.getDate("date_mise_a_jour") != null ? resultSet.getDate("date_mise_a_jour").toLocalDate() : null,
                        resultSet.getString("details")
                );
                dossiers.add(dossier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dossiers;
    }


    public static void updateDossierMedical(DossierMedical dossierMedical) {
        String query = "UPDATE dossier_medical SET patient_id = ?, date_creation = ?, date_mise_a_jour = ?, details = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dossierMedical.getPatient().getId());
            statement.setDate(2, Date.valueOf(dossierMedical.getDateCreation()));
            statement.setDate(3, dossierMedical.getDateMiseAJour() != null ? Date.valueOf(dossierMedical.getDateMiseAJour()) : null);
            statement.setString(4, dossierMedical.getDetails());
            statement.setInt(5, dossierMedical.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteDossierMedical(int id) {
        String query = "DELETE FROM dossier_medical WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public DossierMedical getDossierMedicalByPatientId(int patientId) {
        String query = "SELECT * FROM dossier_medical WHERE patient_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));

                return new DossierMedical(
                        resultSet.getInt("id"),
                        patient,
                        resultSet.getDate("date_creation").toLocalDate(),
                        resultSet.getDate("date_mise_a_jour") != null ? resultSet.getDate("date_mise_a_jour").toLocalDate() : null,
                        resultSet.getString("details")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
