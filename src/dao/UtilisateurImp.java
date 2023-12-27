package dao;

import entity.Role;
import entity.Roles;
import entity.Utilisateur;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilisateurImp implements IUtilisateur {


    DB db = new DB();
    ResultSet rs;
    int ok;

    public UtilisateurImp() {
    }

    @Override
    public Utilisateur saisir()  {
        Utilisateur user = new Utilisateur();
        Scanner clavier = new Scanner(System.in);
        System.out.println(" ".repeat(60) + "Information d'un utilisateur" + "-".repeat(30));
        System.out.print(" ".repeat(60) + "Nom : ");
        user.setNom(clavier.nextLine());

        System.out.print(" ".repeat(60) + "Prenom : ");
        user.setPrenom(clavier.nextLine());

        user.setEmail(user.generateEmail());

        System.out.print(" ".repeat(60) + "Telephone : ");
        user.setTelephone(clavier.nextLine());

        System.out.print(" ".repeat(60) + "Adresse : ");
        user.setAdresse(clavier.nextLine());

        user.setMotDePasse(this.crypteMotDePasse("passer"));

        boolean exit = true;
        do {
            System.out.println(" ".repeat(60) + "Roles");
            System.out.println(" ".repeat(60) + "1." + Roles.RH);
            System.out.println(" ".repeat(60) + "2." + Roles.comptable);
            System.out.println(" ".repeat(60) + "3." + Roles.employe);
            System.out.print(" ".repeat(60) + "Choisissez une option : ");
            int choix = clavier.nextInt();
            switch (choix) {
                case 1:
                    user.setRole("RH");
                    exit = false;
                    break;
                case 2:
                    user.setRole("comptable");
                    exit = false;
                    break;
                case 3:
                    user.setRole("employe");
                    exit = false;
                    break;
            }
        } while (exit);
        return user;
    }

    @Override
    public int ajouter()  {

        this.systemCls();
        Utilisateur user = this.saisir();
        String request = "INSERT INTO utilisateurs(nom, prenom, email, telephone, adresse, motDePasse, idRole) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, user.getNom());
            this.db.getPstm().setString(2, user.getPrenom());
            this.db.getPstm().setString(3, user.getEmail());
            this.db.getPstm().setString(4, user.getTelephone());
            this.db.getPstm().setString(5, user.getAdresse());
            this.db.getPstm().setString(6, user.getMotDePasse());

            int role = 0;

            if (user.getRole().equals("RH")) {
                role = 2;
            } else if (user.getRole().equals("comptable")) {
                role = 3;
            } else {
                role = 4;
            }
            this.db.getPstm().setInt(7, role);
            this.ok = db.executeMaj();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" ".repeat(60) + "-".repeat(15) + "Utilisateur ajouté" + "-".repeat(15));

        this.systemPause();
        this.systemCls();
        return ok;

    }

    @Override
    public List<Utilisateur> getUtilisateurs() {
        List<Utilisateur> users = new ArrayList<>();
        String request = "SELECT * FROM utilisateurs user, roles R WHERE user.idRole = R.id AND user.id != 1";
        try {
            this.db.initPrepare(request);
            this.rs = this.db.executeSelect();
            while (this.rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(this.rs.getInt("user.id"));
                user.setNom(this.rs.getString("user.nom"));
                user.setPrenom(this.rs.getString("user.prenom"));
                user.setEmail(this.rs.getString("user.email"));
                user.setTelephone(this.rs.getString("user.telephone"));
                user.setAdresse(this.rs.getString("user.adresse"));
                user.setMotDePasse(this.rs.getString("user.motDePasse"));
                user.setRole(this.rs.getString("R.nom"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void lister() {
        this.systemCls();
        List<Utilisateur> users = this.getUtilisateurs();
        System.out.println("-".repeat(164));
        System.out.print("| # |");
        System.out.printf(" ".repeat(7) + "%s" + " ".repeat(7), "Nom");
        System.out.print("|");
        System.out.printf(" ".repeat(6) + "%s" + " ".repeat(6), "Prenom");
        System.out.print("|");
        System.out.printf(" ".repeat(8) + "%s" + " ".repeat(8), "Email");
        System.out.print("|");
        System.out.printf(" ".repeat(5) + "%s" + " ".repeat(5), "Telephone");
        System.out.print("|");
        System.out.printf(" ".repeat(5) + "%s" + " ".repeat(5), "Adresse");
        System.out.print("|");
        System.out.printf(" ".repeat(3) + "%s" + " ".repeat(3), "Mot de passe");
        System.out.print("|");
        System.out.printf(" ".repeat(2) + "%s" + " ".repeat(2), "Mot de passe crypté");
        System.out.print("|");
        System.out.printf(" ".repeat(6) + "%s" + " ".repeat(6), "Rôle");
        System.out.print("|\n");
        System.out.println("-".repeat(164));

        int compteur = 1;
        for (Utilisateur user : users) {
            System.out.printf("| %d |", compteur);
            System.out.printf("%13s", user.getNom());
            System.out.printf("%5s", "|");
            System.out.printf("%13s", user.getPrenom());
            System.out.printf("%6s", "|");
            System.out.printf("%19s", user.getEmail());
            System.out.printf("%3s", "|");
            System.out.printf("%15s", user.getTelephone());
            System.out.printf("%5s", "|");
            System.out.printf("%15s", user.getAdresse());
            System.out.printf("%3s", "|");
            System.out.printf("%15s", this.decrypteMotDePasse(user.getMotDePasse()));
            System.out.printf("%4s", "|");

            System.out.printf("%13s", user.getMotDePasse());
            System.out.printf("%7s", "|");
            System.out.printf("%12s", user.getRole());
            System.out.printf("%5s\n", "|");

            compteur++;

//
//            System.out.printf("%30s","-----");
//            System.out.printf("%30s", user.getEmail());
//
//            System.out.printf("%40s","-----");
//            System.out.printf("%40s", user.getTelephone());
//
//            System.out.printf("%50s","-----");
//            System.out.printf("%50s", user.getAdresse());
//
//            System.out.printf("%60s","-----");
//            System.out.printf("%60s", user.getMotDePasse());
        }
        System.out.println("-".repeat(164));

        this.systemPause();
        this.systemCls();

    }


    @Override
    public int modifier()  {
        this.systemCls();
        Scanner clavier = new Scanner(System.in);
        do {
            System.out.print(" ".repeat(60) + "Entrer l'identifiant de l'utilisateur : ");
            int id = clavier.nextInt();
            Utilisateur user = this.get(id);
            if (user != null) {
//
                Utilisateur newUser = this.saisir();
                String request = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ?, motDePasse = ? WHERE id = ?";
                try {
                    this.db.initPrepare(request);
                    this.db.getPstm().setString(1, newUser.getNom());
                    this.db.getPstm().setString(2, newUser.getPrenom());
                    this.db.getPstm().setString(3, newUser.getEmail());
                    this.db.getPstm().setString(4, newUser.getTelephone());
                    this.db.getPstm().setString(5, newUser.getAdresse());
                    this.db.getPstm().setString(6, newUser.getMotDePasse());
                    this.db.getPstm().setInt(7, user.getId());
                    this.ok = db.executeMaj();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                System.out.println(" ".repeat(60) + "Utilisateur inexistant!");
            }
        } while (true);
        System.out.println(" ".repeat(60) + "-".repeat(15) + "Utilisateur modifié" + "-".repeat(15));

        this.systemPause();
        this.systemCls();
        return this.ok;

    }

    @Override
    public int supprimer() {
        this.systemCls();
        Scanner clavier = new Scanner(System.in);
        do {
            System.out.print(" ".repeat(60) + "Entrer l'identifiant de l'utilisateur : ");
            int id = clavier.nextInt();
            if (this.get(id) != null) {
                String request = "DELETE FROM utilisateurs WHERE id = ?";
                try {
                    this.db.initPrepare(request);
                    this.db.getPstm().setInt(1, id);
                    this.ok = this.db.executeMaj();
                    this.db.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                System.out.println(" ".repeat(60) + "Utilisateur inexistant!");
            }
        } while (true);
        System.out.println(" ".repeat(60) + "-".repeat(15) + "Utilisateur supprimé" + "-".repeat(15));

        this.systemPause();
        this.systemCls();
        return this.ok;
    }

    public Utilisateur get(int id) {
        String request = "SELECT * FROM utilisateurs WHERE id = ?";
        Utilisateur user = null;
        try {
            this.db.initPrepare(request);
            this.db.getPstm().setInt(1, id);
            this.rs = this.db.executeSelect();
            if (this.rs.next()) {
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

    public Utilisateur seConnecter()  {
        Scanner clavier = new Scanner(System.in);
        System.out.println(" ".repeat(60) + "Connexion" + "-".repeat(30));
        System.out.print(" ".repeat(60) + "| Email : ");
        String email = clavier.nextLine();
        String motDePasse = "";

        System.out.print(" ".repeat(60) + "| Mot de passe : ");
        motDePasse = clavier.nextLine();



//        System.out.println();
//        System.out.println(" ".repeat(60) + "-".repeat(15) + "Bienvenue" + "-".repeat(15));


        String request = "SELECT * FROM utilisateurs U, roles R WHERE email = ? AND motDePasse = ? AND U.idRole = R.id";
        Utilisateur user = null;
        try {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, email);
            this.db.getPstm().setString(2, crypteMotDePasse(motDePasse));
            this.rs = this.db.executeSelect();
            if (this.rs.next()) {
                String motDePasse1 = null;
                String motDePasse2 = null;

                boolean changed = false; // Permet de verifier si le mot de passe a été changé
                String motDePasseDecrypte = this.rs.getString("U.motDePasse");


                if (motDePasseDecrypte.equals(this.decrypteMotDePasse("passer"))) {
                    changed = true;
                    do {
                        System.out.println(" ".repeat(60) + "Modification des informations" + "-".repeat(30));
                        System.out.println(" ".repeat(60) + "Le mot de passe doit contenir exactement 5 caracteres");

                        System.out.print(" ".repeat(60) + "| Mot de passe : ");
                        motDePasse1 = clavier.nextLine();

                        System.out.print(" ".repeat(60) + "| Confirmation du mot de passe : ");
                        motDePasse2 = clavier.nextLine();

                        if (motDePasse1.equals(motDePasse2)) {
                            break;
                        } else {
                            System.out.println(" ".repeat(60) + "Mots de passe non identiques");
                        }
                    } while (true);
                }
                user = new Utilisateur();
                user.setId(this.rs.getInt("U.id"));
                user.setNom(this.rs.getString("U.nom"));
                user.setPrenom(this.rs.getString("U.prenom"));
                user.setEmail(this.rs.getString("U.email"));
                user.setTelephone(this.rs.getString("U.telephone"));
                user.setAdresse(this.rs.getString("U.adresse"));
                String mdp = (changed) ? motDePasse1 : this.rs.getString("U.motDePasse");
                user.setMotDePasse(this.crypteMotDePasse(mdp));
                user.setRole(this.rs.getString("R.nom"));

                this.modifierMotDePasse(user.getMotDePasse(), user.getId());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

    public void menu(Utilisateur user)  {
        Scanner clavier = new Scanner(System.in);
        if (user.getId() == 1) {
            this.adminMenu();
        } else if (user.getId() == 2) {
            this.RHMenu();
        } else {
            this.autreMenu();
        }
    }

    public void adminMenu()  {
        this.systemPause();
        this.systemCls();
        Scanner clavier = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println(" ".repeat(60) + "Menu" + "-".repeat(30));

            System.out.println(" ".repeat(59) + "| 1.Lister");
            System.out.println(" ".repeat(59) + "| 2.Ajouter");
            System.out.println(" ".repeat(59) + "| 3.Modifier");
            System.out.println(" ".repeat(59) + "| 4.Supprimer");
            System.out.println(" ".repeat(59) + "| 5.Quitter");
            System.out.print(" ".repeat(59) + "| Selectionner une action : ");
            int choix1 = clavier.nextInt();
            switch (choix1) {
                case 1:
                    this.lister();
                    break;
                case 2:
                    this.ajouter();
                    break;
                case 3:
                    this.modifier();
                    break;
                case 4:
                    this.supprimer();
                    break;
                case 5:
                    exit = false;
            }
        } while (exit);
    }

    public void RHMenu()  {
        this.systemPause();
        this.systemCls();
        Scanner clavier = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println(" ".repeat(60) + "Menu" + "-".repeat(30));

            System.out.println(" ".repeat(59) + "| 1.Lister");
            System.out.println(" ".repeat(59) + "| 2.Ajouter");
            System.out.println(" ".repeat(59) + "| 3.Modifier");
            System.out.println(" ".repeat(59) + "| 4.Quitter");
            System.out.print(" ".repeat(59) + "| Selectionner une action : ");
            int choix1 = clavier.nextInt();
            switch (choix1) {
                case 1:
                    this.lister();
                    break;
                case 2:
                    this.ajouter();
                    break;
                case 3:
                    this.modifier();
                    break;
                case 4:
                    exit = false;
            }
        } while (exit);
    }

    public void autreMenu() {
        this.systemPause();
        this.systemCls();
        Scanner clavier = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println(" ".repeat(60) + "Menu" + "-".repeat(30));
            System.out.println(" ".repeat(59) + "| 1.Lister");
            System.out.println(" ".repeat(59) + "| 2.Quitter");
            System.out.print(" ".repeat(59) + "| Selectionner une action : ");
            int choix1 = clavier.nextInt();
            switch (choix1) {
                case 1:
                    this.lister();
                    break;
                case 2:
                    exit = false;
            }
        } while (exit);
    }

    public int modifierMotDePasse(String motDePasse, int id) {
        String request = "UPDATE utilisateurs SET motDePasse = ? WHERE id = ?";
        try {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, motDePasse);
            this.db.getPstm().setInt(2, id);
            this.ok = this.db.executeMaj();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ok;
    }

    public void systemCls() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public void systemPause() {
        System.out.print(" ".repeat(60) + "Appuyez sur une touche pour continuer...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public String crypteMotDePasse(String motDePasse) {
        StringBuilder motDePasseCrypte= new StringBuilder();
        for (int i = 0; i < motDePasse.length(); i++) {
//            System.out.println((Character.toString(mdp1.charAt(i)+(i+32))));
            motDePasseCrypte.append(Character.reverseBytes(motDePasse.charAt(i)));

        }
        return motDePasseCrypte.toString();
    }

    public String decrypteMotDePasse(String motDePasse) {
        StringBuilder motDePasseDecrypte = new StringBuilder();
        for (int i = 0; i < motDePasse.length(); i++) {
//            System.out.println((Character.toString(mdp1.charAt(i)+(i+32))));
            motDePasseDecrypte.append(Character.reverseBytes(motDePasse.charAt(i)));

        }
        return motDePasseDecrypte.toString();
    }
}




