package Main;

import dao.DB;
import dao.IUtilisateur;
import dao.UtilisateurImp;
import entity.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args)  {
        DB db = new DB();
        db.getConnection();
        IUtilisateur iUtilisateur = new UtilisateurImp();
        Utilisateur connected;
        do
        {
            connected = iUtilisateur.seConnecter();
            if(connected != null)
            {
                break;
            }else
            {
                System.out.println(" ".repeat(60) + "Identifiants incorrect");
            }
        }while(true);
        iUtilisateur.menu(connected);
    }

}