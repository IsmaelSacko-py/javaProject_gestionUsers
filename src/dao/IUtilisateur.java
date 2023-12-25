package dao;

import entity.Role;
import entity.Utilisateur;

import java.util.List;

public interface IUtilisateur
{
    Utilisateur saisir();
    int ajouter();
    List<Utilisateur> getUtilisateurs();
    void lister();
    int modifier();
    int supprimer();
    Role getRole(int id);

    Utilisateur get(int id);

    Utilisateur seConnecter();

    void menu(Utilisateur user);

    void adminMenu();
    void RHMenu();
}
