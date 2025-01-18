package org.najdclinique;

public class Administrateur {
    private int id;
    private String username;
    private String password;
    private String photoProfil;


    public Administrateur() {}


    public Administrateur(int id, String username, String password, String photoProfil) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.photoProfil = photoProfil;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    @Override
    public String toString() {
        return "Administrateur{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", photoProfil='" + photoProfil + '\'' +
                '}';
    }
}