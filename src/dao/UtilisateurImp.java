package dao;

import entity.Role;
import entity.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilisateurImp implements IUtilisateur
{

    DB db = new DB();
    ResultSet rs;
    int ok;
    @Override
    public Utilisateur saisir()
    {
        Utilisateur user = new Utilisateur();
        Scanner clavier = new Scanner(System.in);

        System.out.println("Nom : ");
        user.setNom(clavier.nextLine());

        System.out.println("Prenom : ");
        user.setPrenom(clavier.nextLine());

        System.out.println("Email : ");
        user.setEmail(clavier.nextLine());

        System.out.println("Telephone : ");
        user.setTelephone(clavier.nextLine());

        System.out.println("Adresse : ");
        user.setAdresse(clavier.nextLine());

        do {
            System.out.println("Mot de passe : ");
            String motDePasse1 = clavier.nextLine();

            System.out.println("Confirmer mot de passe : ");
            String motDePasse2 = clavier.nextLine();

            if(motDePasse1.equals(motDePasse2))
            {
                user.setMotDePasse(motDePasse1);
                break;
            }else
            {
                System.out.println("Mots de passe non identiques");
            }
        }while(true);

        return user;
    }

    @Override
    public int ajouter(Utilisateur user)
    {
        String request = "INSERT INTO utilisateurs(nom, prenom, email, telephone, adresse, motDePasse, idRole) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try
        {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, user.getNom());
            this.db.getPstm().setString(2, user.getPrenom());
            this.db.getPstm().setString(3, user.getEmail());
            this.db.getPstm().setString(4, user.getTelephone());
            this.db.getPstm().setString(5, user.getAdresse());
            this.db.getPstm().setString(6, user.getMotDePasse());
            this.db.getPstm().setInt(7, 2);
            this.ok = db.executeMaj();

        }catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Utilisateur> getUtilisateurs()
    {
        List<Utilisateur> users = new ArrayList<>();
        String request = "SELECT * FROM utilisateurs user";
        try
        {
            this.db.initPrepare(request);
            this.rs = this.db.executeSelect();
            while(this.rs.next())
            {
                Utilisateur user = new Utilisateur();
                user.setId(this.rs.getInt("user.id"));
                user.setNom(this.rs.getString("user.nom"));
                user.setPrenom(this.rs.getString("user.prenom"));
                user.setEmail(this.rs.getString("user.email"));
                user.setTelephone(this.rs.getString("user.telephone"));
                user.setAdresse(this.rs.getString("user.adresse"));
                user.setMotDePasse(this.rs.getString("user.motDePasse"));
                users.add(user);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return users;
    }

    public void lister()
    {
        List<Utilisateur> users = this.getUtilisateurs();
        for (Utilisateur user : users)
        {
            System.out.println("-------------------------------------");
            System.out.println("Id : " + user.getId());
            System.out.println("Nom : " + user.getNom());
            System.out.println("Prenom : " + user.getPrenom());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Telephone : " + user.getTelephone());
            System.out.println("Adresse : " + user.getAdresse());
            System.out.println("Mot de passe : " + user.getMotDePasse());
        }
    }


    @Override
    public int modifier(Utilisateur user)
    {
        String request = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ?, motDePasse = ? WHERE id = ?";
        try
        {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, user.getNom());
            this.db.getPstm().setString(2, user.getPrenom());
            this.db.getPstm().setString(3, user.getEmail());
            this.db.getPstm().setString(4, user.getTelephone());
            this.db.getPstm().setString(5, user.getAdresse());
            this.db.getPstm().setString(6, user.getMotDePasse());
            this.db.getPstm().setInt(7, user.getId());
            this.ok = db.executeMaj();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.ok;
    }

    @Override
    public int supprimer(int id)
    {
        String request = "DELETE FROM utilisateurs WHERE id = ?";
        try
        {
            this.db.initPrepare(request);
            this.db.getPstm().setInt(1, id);
            this.ok = this.db.executeMaj();
            this.db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.ok;
    }

    public Utilisateur get(int id)
    {
        String request = "SELECT * FROM utilisateurs WHERE id = ?";
        Utilisateur user = null;
        try
        {
            this.db.initPrepare(request);
            this.db.getPstm().setInt(1, id);
            this.rs = this.db.executeSelect();
            if(this.rs.next())
            {
                user = new Utilisateur();
                user.setId(this.rs.getInt("id"));
                user.setNom(this.rs.getString("nom"));
                user.setPrenom(this.rs.getString("prenom"));
                user.setEmail(this.rs.getString("email"));
                user.setTelephone(this.rs.getString("telephone"));
                user.setAdresse(this.rs.getString("adresse"));
                user.setMotDePasse(this.rs.getString("motDePasse"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Role getRole(int id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        Role role = null;

        try {
            this.db.initPrepare(sql);
            this.db.getPstm().setInt(1, id);
            this.rs = this.db.executeSelect();
            if (this.rs.next()) {
                role = new Role();
                role.setId(this.rs.getInt("id"));
                role.setNom(this.rs.getString("nom"));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return role;
    }

    public Utilisateur seConnecter()
    {
        Scanner clavier = new Scanner(System.in);
        System.out.println("Email : ");
        String email = clavier.nextLine();

        System.out.println("Mot de passe : ");
        String motDePasse = clavier.nextLine();

        String request = "SELECT * FROM utilisateurs WHERE email = ? AND motDePasse = ?";
        Utilisateur user = null;
        try
        {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, email);
            this.db.getPstm().setString(2, motDePasse);
            this.rs = this.db.executeSelect();
            if(this.rs.next())
            {
                user = new Utilisateur();
                user.setId(this.rs.getInt("id"));
                user.setNom(this.rs.getString("nom"));
                user.setPrenom(this.rs.getString("prenom"));
                user.setEmail(this.rs.getString("email"));
                user.setTelephone(this.rs.getString("telephone"));
                user.setAdresse(this.rs.getString("adresse"));
                user.setMotDePasse(this.rs.getString("motDePasse"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
