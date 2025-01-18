package org.najdclinique;


public class Facture {
    private int id;
    private double montant;
    private String details;
    private Patient patient;

    public Facture() {}

    public Facture(int id, double montant, String details, Patient patient) {
        this.id = id;
        this.montant = montant;
        this.details = details;
        this.patient = patient;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", montant=" + montant +
                ", details='" + details + '\'' +
                ", patient=" + patient +
                '}';
    }
}
