package org.najdclinique;


import java.time.LocalDate;

public class Chambre {
    private int id;
    private int numero;
    private int capacite;
    private Etat etat;
    private LocalDate dateOccupation;
    private LocalDate dateLiberation;


    public Chambre() {}


    public Chambre(int id, int numero, int capacite, Etat etat, LocalDate dateOccupation, LocalDate dateLiberation) {
        this.id = id;
        this.numero = numero;
        this.capacite = capacite;
        this.etat = etat;
        this.dateOccupation = dateOccupation;
        this.dateLiberation = dateLiberation;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public LocalDate getDateOccupation() {
        return dateOccupation;
    }

    public void setDateOccupation(LocalDate dateOccupation) {
        this.dateOccupation = dateOccupation;
    }

    public LocalDate getDateLiberation() {
        return dateLiberation;
    }

    public void setDateLiberation(LocalDate dateLiberation) {
        this.dateLiberation = dateLiberation;
    }

    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacite=" + capacite +
                ", etat=" + etat +
                ", dateOccupation=" + dateOccupation +
                ", dateLiberation=" + dateLiberation +
                '}';
    }
}
