package org.najdclinique;


import java.util.Date;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private Genre genre;
    private String photoProfil;
    private Date dateNaissance;
    private String email;
    private String numTel;
    private DossierMedical dossierMedical;


    public Patient() {}

    public Patient(int id, String nom, String prenom, Genre genre, String photoProfil, Date dateNaissance, String email, String numTel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.photoProfil = photoProfil;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.numTel = numTel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        this.dossierMedical = dossierMedical;
    }

    @Override
    public String toString() {
        return nom + " " + prenom; // Display nom and prenom
    }
}
