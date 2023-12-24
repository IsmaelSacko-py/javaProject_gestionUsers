package dao;

import entity.Role;
import entity.Utilisateur;

import java.util.List;

public interface IUtilisateur
{
    Utilisateur saisir();
    int ajouter(Utilisateur utilisateur);
    List<Utilisateur> getUtilisateurs();
    void lister();
    int modifier(Utilisateur user);
    int supprimer(int id);
    Role getRole(int id);

    Utilisateur get(int id);

    Utilisateur seConnecter();
}
