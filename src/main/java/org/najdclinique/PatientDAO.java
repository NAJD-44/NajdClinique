package org.najdclinique;



import org.najdclinique.Patient;
import org.najdclinique.Genre;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void addPatient(Patient patient) {
        String query = "INSERT INTO patient (nom, prenom, genre, photo_profil, date_naissance, email, num_tel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getNom());
            statement.setString(2, patient.getPrenom());
            statement.setString(3, patient.getGenre().name());
            statement.setString(4, patient.getPhotoProfil());
            statement.setDate(5, new Date(patient.getDateNaissance().getTime()));
            statement.setString(6, patient.getEmail());
            statement.setString(7, patient.getNumTel());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Patient getPatientById(int id) {
        String query = "SELECT * FROM patient WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patient(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        Genre.valueOf(resultSet.getString("genre")),
                        resultSet.getString("photo_profil"),
                        resultSet.getDate("date_naissance"),
                        resultSet.getString("email"),
                        resultSet.getString("num_tel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Patient getPatientByIdF(int id) {
        String query = "SELECT nom, prenom FROM patient WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Patient patient = new Patient();
                patient.setNom(resultSet.getString("nom"));
                patient.setPrenom(resultSet.getString("prenom"));
                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public static List<Patient> getAllPatientsForDisplay() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT id, nom, prenom FROM patient";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setNom(resultSet.getString("nom"));
                patient.setPrenom(resultSet.getString("prenom"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void updatePatient(Patient patient) {
        String query = "UPDATE patient SET nom = ?, prenom = ?, genre = ?, photo_profil = ?, date_naissance = ?, email = ?, num_tel = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patient.getNom());
            statement.setString(2, patient.getPrenom());
            statement.setString(3, patient.getGenre().name());
            statement.setString(4, patient.getPhotoProfil());
            statement.setDate(5, new Date(patient.getDateNaissance().getTime()));
            statement.setString(6, patient.getEmail());
            statement.setString(7, patient.getNumTel());
            statement.setInt(8, patient.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int id) {
        String query = "DELETE FROM patient WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Patient> getPatientsByGenre(Genre genre) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient WHERE genre = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, genre.name());
            ResultSet resultSet = statement.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
