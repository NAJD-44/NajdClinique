package org.najdclinique;


import org.najdclinique.Infirmier;
import org.najdclinique.Status;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InfirmierDAO {

    public static void addInfirmier(Infirmier infirmier) {
        String query = "INSERT INTO infirmier (nom, prenom, photo_profil, email, password, num_tel, status,) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, infirmier.getNom());
            statement.setString(2, infirmier.getPrenom());
            statement.setString(3, infirmier.getPhotoProfil());
            statement.setString(4, infirmier.getEmail());
            statement.setString(5, infirmier.getPassword());
            statement.setString(6, infirmier.getNumTel());
            statement.setString(7, infirmier.getStatus().name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    infirmier.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Infirmier getInfirmierById(int id) {
        String query = "SELECT * FROM infirmier WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Infirmier(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
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

    public static List<Infirmier> getAllInfirmiers() {
        List<Infirmier> infirmiers = new ArrayList<>();
        String query = "SELECT * FROM infirmier";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Infirmier infirmier = new Infirmier(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
                infirmiers.add(infirmier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infirmiers;
    }

    public static void updateInfirmier(Infirmier infirmier) {
        String query = "UPDATE infirmier SET nom = ?, prenom = ?, photo_profil = ?, email = ?, password = ?, num_tel = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, infirmier.getNom());
            statement.setString(2, infirmier.getPrenom());
            statement.setString(3, infirmier.getPhotoProfil());
            statement.setString(4, infirmier.getEmail());
            statement.setString(5, infirmier.getPassword());
            statement.setString(6, infirmier.getNumTel());
            statement.setString(7, infirmier.getStatus().name());
            statement.setInt(8, infirmier.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteInfirmier(int id) {
        String query = "DELETE FROM infirmier WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Infirmier getInfirmierByEmail(String email) {
        String query = "SELECT * FROM infirmier WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Infirmier(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
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
}
