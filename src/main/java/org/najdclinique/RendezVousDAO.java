package org.najdclinique;



import org.najdclinique.RendezVous;
import org.najdclinique.Patient;
import org.najdclinique.Medecin;
import org.najdclinique.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {


    public static void addRendezVous(RendezVous rendezVous) {
        String query = "INSERT INTO rendez_vous (date_hour, patient_id, medecin_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setTimestamp(1, new Timestamp(rendezVous.getDateHour().getTime()));
            statement.setInt(2, rendezVous.getPatient().getId());
            statement.setInt(3, rendezVous.getMedecin().getId());
            statement.executeUpdate();


            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rendezVous.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public RendezVous getRendezVousById(int id) {
        String query = "SELECT * FROM rendez_vous WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                MedecinDAO medecinDAO = new MedecinDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));
                Medecin medecin = medecinDAO.getMedecinById(resultSet.getInt("medecin_id"));

                return new RendezVous(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date_hour"),
                        patient,
                        medecin
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<RendezVous> getAllRendezVous() {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String query = "SELECT * FROM rendez_vous";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                MedecinDAO medecinDAO = new MedecinDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));
                Medecin medecin = medecinDAO.getMedecinById(resultSet.getInt("medecin_id"));

                RendezVous rendezVous = new RendezVous(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date_hour"),
                        patient,
                        medecin
                );
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    public static void updateRendezVous(RendezVous rendezVous) {
        String query = "UPDATE rendez_vous SET date_hour = ?, patient_id = ?, medecin_id = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(rendezVous.getDateHour().getTime()));
            statement.setInt(2, rendezVous.getPatient().getId());
            statement.setInt(3, rendezVous.getMedecin().getId());
            statement.setInt(4, rendezVous.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRendezVous(int id) {
        String query = "DELETE FROM rendez_vous WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RendezVous> getRendezVousByPatientId(int patientId) {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String query = "SELECT * FROM rendez_vous WHERE patient_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                MedecinDAO medecinDAO = new MedecinDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));
                Medecin medecin = medecinDAO.getMedecinById(resultSet.getInt("medecin_id"));

                RendezVous rendezVous = new RendezVous(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date_hour"),
                        patient,
                        medecin
                );
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    public List<RendezVous> getRendezVousByMedecinId(int medecinId) {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String query = "SELECT * FROM rendez_vous WHERE medecin_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, medecinId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PatientDAO patientDAO = new PatientDAO();
                MedecinDAO medecinDAO = new MedecinDAO();
                Patient patient = patientDAO.getPatientById(resultSet.getInt("patient_id"));
                Medecin medecin = medecinDAO.getMedecinById(resultSet.getInt("medecin_id"));

                RendezVous rendezVous = new RendezVous(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date_hour"),
                        patient,
                        medecin
                );
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }
}
