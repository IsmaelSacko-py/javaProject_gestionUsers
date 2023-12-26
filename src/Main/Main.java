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

        Utilisateur connected = iUtilisateur.get(1);
//        do
//        {
//            connected = iUtilisateur.seConnecter();
//            if(connected != null)
//            {
//                break;
//            }else
//            {
//                System.out.println("Identifiants incorrect");
//            }
//        }while(true);

        iUtilisateur.lister();
    }

}