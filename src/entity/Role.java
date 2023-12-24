package entity;

import java.util.ArrayList;

public class Role
{
    private String nom;
    private ArrayList<Utilisateur> listeUtilisateur;

    public Role(String nom)
    {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
