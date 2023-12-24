package Main;

import dao.DB;
import dao.IUtilisateur;
import dao.UtilisateurImp;
import entity.Utilisateur;

public class Main {
    public static void main(String[] args)
    {
        DB db = new DB();
        db.getConnection();
        IUtilisateur iUtilisateur = new UtilisateurImp();

//        for (int i = 0; i < 3; i++) {
//            iUtilisateur.ajouter(iUtilisateur.saisir());
//        }

        Utilisateur user = iUtilisateur.get(4);
        user.setNom("Odzounalo OFOU");
        user.setPrenom("Hance Cruz");
        iUtilisateur.modifier(user);
        iUtilisateur.lister();
    }
}