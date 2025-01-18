package org.najdclinique;



import org.najdclinique.Facture;
import org.najdclinique.Patient;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public static void addFacture(Facture facture) {
        String query = "INSERT INTO facture (montant, details, patient_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, facture.getMontant());
            statement.setString(2, facture.getDetails());
            statement.setInt(3, facture.getPatient().getId());
            statement.executeUpdate();


            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    facture.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Facture getFactureById(int id) {
        String query = "SELECT * FROM facture WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));

                return new Facture(
                        resultSet.getInt("id"),
                        resultSet.getDouble("montant"),
                        resultSet.getString("details"),
                        patient
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Facture> getAllFactures() {
        List<Facture> factures = new ArrayList<>();
        String query = "SELECT * FROM facture";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientByIdF(resultSet.getInt("patient_id"));

                Facture facture = new Facture(
                        resultSet.getInt("id"),
                        resultSet.getDouble("montant"),
                        resultSet.getString("details"),
                        patient
                );
                factures.add(facture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    public static void updateFacture(Facture facture) {
        String query = "UPDATE facture SET montant = ?, details = ?, patient_id = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, facture.getMontant());
            statement.setString(2, facture.getDetails());
            statement.setInt(3, facture.getPatient().getId());
            statement.setInt(4, facture.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFacture(int id) {
        String query = "DELETE FROM facture WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Facture> getFacturesByPatientId(int patientId) {
        List<Facture> factures = new ArrayList<>();
        String query = "SELECT * FROM facture WHERE patient_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));

                Facture facture = new Facture(
                        resultSet.getInt("id"),
                        resultSet.getDouble("montant"),
                        resultSet.getString("details"),
                        patient
                );
                factures.add(facture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }
}
