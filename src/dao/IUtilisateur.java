package dao;

import entity.Role;
import entity.Utilisateur;

import java.util.List;

public interface IUtilisateur
{


    String crypteMotDePasse(String motDePasse);

    boolean checkMotDePasse(String motDePasse, String motDePasse2);
    Utilisateur saisir() ;
    int ajouter() ;
    List<Utilisateur> getUtilisateurs();
    void lister();
    int modifier() ;
    int supprimer();

    Utilisateur get(int id);

    Utilisateur seConnecter() ;

    void menu(Utilisateur user) ;

    void adminMenu() ;
    void RHMenu() ;

    int modifierMotDePasse(String motDePasse, int id);

    void systemPause();
    void systemCls();

    List<List<String>> convertir(List<Utilisateur> users);

    void creerTableau(List<List<String>> tableauUtilisateur);
}
