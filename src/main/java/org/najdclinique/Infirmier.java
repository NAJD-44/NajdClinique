package org.najdclinique;

import java.util.List;

public class Infirmier {
    private int id;
    private String nom;
    private String prenom;
    private String photoProfil;
    private String email;
    private String password;
    private String numTel;
    private Status status;
    private List<Chambre> chambres;

    public Infirmier() {}

    public Infirmier(int id, String nom, String prenom, String photoProfil, String email, String password, String numTel, Status status) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.photoProfil = photoProfil;
        this.email = email;
        this.password = password;
        this.numTel = numTel;
        this.status = status;
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

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }

    @Override
    public String toString() {
        return "Infirmier{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", photoProfil='" + photoProfil + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", numTel='" + numTel + '\'' +
                ", status=" + status +
                '}';
    }
}
