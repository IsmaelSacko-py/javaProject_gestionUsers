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
        do
        {
            connected = iUtilisateur.seConnecter();
            if(connected != null)
            {
                break;
            }else
            {
                System.out.println("Identifiants incorrect");
            }
        }while(true);

        iUtilisateur.menu(connected);
//        System.out.println("Id : " + connected.getId());
//        System.out.println("Nom : " + connected.getNom());
//        System.out.println("Prenom : " + connected.getPrenom());
//        System.out.println("Email : " + connected.getEmail());
//        System.out.println("Telephone : " + connected.getTelephone());
//        System.out.println("Adresse : " + connected.getAdresse());
//        System.out.println("Mot de passe : " + connected.getMotDePasse());
//        System.out.println("Role : " + connected.getRole());
    }

}