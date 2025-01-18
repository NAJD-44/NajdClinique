package org.najdclinique;


import java.util.Date;

public class RendezVous {
    private int id;
    private Date dateHour;
    private Patient patient;
    private Medecin medecin;

    public RendezVous() {}


    public RendezVous(int id, Date dateHour, Patient patient, Medecin medecin) {
        this.id = id;
        this.dateHour = dateHour;
        this.patient = patient;
        this.medecin = medecin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateHour() {
        return dateHour;
    }

    public void setDateHour(Date dateHour) {
        this.dateHour = dateHour;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", dateHour=" + dateHour +
                ", patient=" + patient +
                ", medecin=" + medecin +
                '}';
    }
}
