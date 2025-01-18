package org.najdclinique;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DossierMedical {
    private int id;
    private Patient patient;
    private LocalDate dateCreation;
    private LocalDate dateMiseAJour;
    private List<RendezVous> rendezVous = new ArrayList<>();
    private String details;


    public DossierMedical() {}

    public DossierMedical(int id, Patient patient, LocalDate dateCreation, LocalDate dateMiseAJour, String details) {
        this.id = id;
        this.patient = patient;
        this.dateCreation = dateCreation;
        this.dateMiseAJour = dateMiseAJour;
        this.details = details;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDate dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "DossierMedical{" +
                "id=" + id +
                ", patient=" + patient +
                ", dateCreation=" + dateCreation +
                ", dateMiseAJour=" + dateMiseAJour +
                ", details='" + details + '\'' +
                '}';
    }
}
