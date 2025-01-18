package org.najdclinique;

import org.najdclinique.Administrateur;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministrateurDAO {


    public static void addAdministrateur(Administrateur administrateur) {
        String query = "INSERT INTO administrateur (username, password, photo_profil) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, administrateur.getUsername());
            statement.setString(2, administrateur.getPassword());
            statement.setString(3, administrateur.getPhotoProfil());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Administrateur getAdministrateurById(int id) {
        String query = "SELECT * FROM administrateur WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Administrateur(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("photo_profil")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Administrateur> getAllAdministrateurs() {
        List<Administrateur> administrateurs = new ArrayList<>();
        String query = "SELECT * FROM administrateur";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Administrateur administrateur = new Administrateur(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("photo_profil")
                );
                administrateurs.add(administrateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrateurs;
    }

    public static void updateAdministrateur(Administrateur administrateur) {
        String query = "UPDATE administrateur SET username = ?, password = ?, photo_profil = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, administrateur.getUsername());
            statement.setString(2, administrateur.getPassword());
            statement.setString(3, administrateur.getPhotoProfil());
            statement.setInt(4, administrateur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteAdministrateur(int id) {
        String query = "DELETE FROM administrateur WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Administrateur getAdministrateurByUsername(String username) {
        String query = "SELECT * FROM administrateur WHERE username = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Administrateur(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("photo_profil")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}