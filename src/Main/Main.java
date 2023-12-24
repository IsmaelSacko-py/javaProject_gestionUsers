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

        do
        {
            Utilisateur connected = iUtilisateur.seConnecter();
            if (connected == null)
            {
                System.out.println("Identifiants incorrect");
            }else
            {
                System.out.println("Connectée");
                break;
            }

        }while(true);

    }
}