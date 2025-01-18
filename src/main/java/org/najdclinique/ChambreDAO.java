package org.najdclinique;


import org.najdclinique.Chambre;
import org.najdclinique.Etat;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAO {


    public static void addChambre(Chambre chambre) {
        String query = "INSERT INTO chambre (numero, capacite, etat, date_occupation, date_liberation) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chambre.getNumero());
            statement.setInt(2, chambre.getCapacite());
            statement.setString(3, chambre.getEtat().name());
            statement.setDate(4, Date.valueOf(chambre.getDateOccupation()));
            statement.setDate(5, chambre.getDateLiberation() != null ? Date.valueOf(chambre.getDateLiberation()) : null);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Chambre getChambreById(int id) {
        String query = "SELECT * FROM chambre WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Chambre(
                        resultSet.getInt("id"),
                        resultSet.getInt("numero"),
                        resultSet.getInt("capacite"),
                        Etat.valueOf(resultSet.getString("etat")),
                        resultSet.getDate("date_occupation").toLocalDate(),
                        resultSet.getDate("date_liberation") != null ? resultSet.getDate("date_liberation").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Chambre> getAllChambres() {
        List<Chambre> chambres = new ArrayList<>();
        String query = "SELECT * FROM chambre";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Chambre chambre = new Chambre(
                        resultSet.getInt("id"),
                        resultSet.getInt("numero"),
                        resultSet.getInt("capacite"),
                        Etat.valueOf(resultSet.getString("etat")),
                        resultSet.getDate("date_occupation").toLocalDate(),
                        resultSet.getDate("date_liberation") != null ? resultSet.getDate("date_liberation").toLocalDate() : null
                );
                chambres.add(chambre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chambres;
    }


    public static void updateChambre(Chambre chambre) {
        String query = "UPDATE chambre SET numero = ?, capacite = ?, etat = ?, date_occupation = ?, date_liberation = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chambre.getNumero());
            statement.setInt(2, chambre.getCapacite());
            statement.setString(3, chambre.getEtat().name());
            statement.setDate(4, Date.valueOf(chambre.getDateOccupation()));
            statement.setDate(5, chambre.getDateLiberation() != null ? Date.valueOf(chambre.getDateLiberation()) : null);
            statement.setInt(6, chambre.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteChambre(int id) {
        String query = "DELETE FROM chambre WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Chambre> getChambresByEtat(Etat etat) {
        List<Chambre> chambres = new ArrayList<>();
        String query = "SELECT * FROM chambre WHERE etat = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, etat.name());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Chambre chambre = new Chambre(
                        resultSet.getInt("id"),
                        resultSet.getInt("numero"),
                        resultSet.getInt("capacite"),
                        Etat.valueOf(resultSet.getString("etat")),
                        resultSet.getDate("date_occupation").toLocalDate(),
                        resultSet.getDate("date_liberation") != null ? resultSet.getDate("date_liberation").toLocalDate() : null
                );
                chambres.add(chambre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chambres;
    }
}
