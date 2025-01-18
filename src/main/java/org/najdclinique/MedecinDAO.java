package org.najdclinique;



import org.najdclinique.Medecin;
import org.najdclinique.Status;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    public static void addMedecin(Medecin medecin) {
        String query = "INSERT INTO medecin (nom, prenom, specialite, photo_profil, email, password, num_tel, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, medecin.getNom());
            statement.setString(2, medecin.getPrenom());
            statement.setString(3, medecin.getSpecialite());
            statement.setString(4, medecin.getPhotoProfil());
            statement.setString(5, medecin.getEmail());
            statement.setString(6, medecin.getPassword());
            statement.setString(7, medecin.getNumTel());
            statement.setString(8, medecin.getStatus().name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medecin.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Medecin getMedecinById(int id) {
        String query = "SELECT * FROM medecin WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Medecin> getAllMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT * FROM medecin";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Medecin medecin = new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }

    public static void updateMedecin(Medecin medecin) {
        String query = "UPDATE medecin SET nom = ?, prenom = ?, specialite = ?, photo_profil = ?, email = ?, password = ?, num_tel = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, medecin.getNom());
            statement.setString(2, medecin.getPrenom());
            statement.setString(3, medecin.getSpecialite());
            statement.setString(4, medecin.getPhotoProfil());
            statement.setString(5, medecin.getEmail());
            statement.setString(6, medecin.getPassword());
            statement.setString(7, medecin.getNumTel());
            statement.setString(8, medecin.getStatus().name());
            statement.setInt(9, medecin.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMedecin(int id) {
        String query = "DELETE FROM medecin WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Medecin> getMedecinsBySpecialite(String specialite) {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT * FROM medecin WHERE specialite = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialite);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Medecin medecin = new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }

    public Medecin getMedecinByEmail(String email) {
        String query = "SELECT * FROM medecin WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Medecin> getAllMedecinsForDisplay() {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT id, nom, prenom FROM medecin";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Medecin medecin = new Medecin();
                medecin.setId(resultSet.getInt("id"));
                medecin.setNom(resultSet.getString("nom"));
                medecin.setPrenom(resultSet.getString("prenom"));
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }
}
