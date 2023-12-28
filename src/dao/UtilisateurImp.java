package dao;

import entity.Role;
import entity.Roles;
import entity.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UtilisateurImp implements IUtilisateur {


    DB db = new DB();
    ResultSet rs;
    int ok;

    public UtilisateurImp() {
    }


    /**
     * Saisie les informations d'un utilisateur excepté email qui est générer
     * et le mot de passe. A leur création, les utilisateurs ont tous un mot
     * de passe par defaut.
     * @return L'utilisateur saisie
     */
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

        user.setMotDePasse("passer");
        user.setMotDePasseHache(this.crypteMotDePasse(user.getMotDePasse()));

//        System.out.println("mdp : " + user.getMotDePasse());
//        System.out.println("mdp crypté : " + user.getMotDePasseHache());

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

    /**
     * Ajoute un utilisateur.
     *
     * @return 1 L'ajout s'est bien passé sinon 0
     */
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
            this.db.getPstm().setString(6, user.getMotDePasseHache());

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

    /**
     * Récupère tous les utilisateurs et leurs rôles excepté l'administrateur.
     * @return La liste des utilisateurs
     */
    @Override
    public List<Utilisateur> getUtilisateurs() {
        List<Utilisateur> users = new ArrayList<>();
        String request = "SELECT * FROM utilisateurs user, roles R WHERE user.idRole = R.id AND user.id != 1";
        try {
            this.db.initPrepare(request);
            this.rs = this.db.executeSelect();
            while (this.rs.next()) {
                //List<Utilisateur> utilisateur = new ArrayList<>();
                Utilisateur user = new Utilisateur();

                user.setId(this.rs.getInt("user.id"));
                user.setNom(this.rs.getString("user.nom"));
                user.setPrenom(this.rs.getString("user.prenom"));
                user.setEmail(this.rs.getString("user.email"));
                user.setTelephone(this.rs.getString("user.telephone"));
                user.setAdresse(this.rs.getString("user.adresse"));
                user.setMotDePasseHache(this.rs.getString("user.motDePasse"));
                user.setRole(this.rs.getString("R.nom"));

                users.add(user);
                //allUsers.add(utilisateur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Liste tous les utilisateurs à l'exception de l'administrateur.
     */
    public void lister() {

        this.systemCls();

        List<List<String>> tableauUtilisateur = this.convertir(this.getUtilisateurs());

        this.creerTableau(tableauUtilisateur);

        this.systemPause();
        this.systemCls();

    }


    /**
     * Modifie un utilisateur en fonction de l'identifiant de l'utilisateur qui sera renseigné:
     * @return 1 lorsque la suppression s'effectue correctement sinon 0;
     */
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

    /**
     * Supprime un utilisateur en fonction de l'identifiant de l'utilisateur qui sera renseigné:
     * @return 1 lorsque la suppression s'effectue correctement sinon 0;
     */
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

    /**
     * Récupère un utilisateur.
     *
     * @param id L'identifiant de l'utilisateur
     *
     * @return L'utilisateur
     */

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
                user.setMotDePasseHache(this.rs.getString("motDePasse"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Permet à un utilisateur de se connecter en entrant son email et son mot de passer.
     * Si l'utilisateur se connecter pour la première fois alors, il est doit obligatoirement
     * changer de mot de passe. Après que tout soit conforme, il est redirigé vers un menu spécifique
     * en fonction de son rôle.
     *
     * @return L'utilisateur connecté
     */

    public Utilisateur seConnecter()  {
        Scanner clavier = new Scanner(System.in);
        System.out.println(" ".repeat(60) + "Connexion" + "-".repeat(30));
        System.out.print(" ".repeat(60) + "| Email : ");
        String email = clavier.nextLine();
        String motDePasseSaisi = "";

        System.out.print(" ".repeat(60) + "| Mot de passe : ");
        motDePasseSaisi = clavier.nextLine();



//        System.out.println();
//        System.out.println(" ".repeat(60) + "-".repeat(15) + "Bienvenue" + "-".repeat(15));


        String request = "SELECT * FROM utilisateurs U, roles R WHERE email = ? AND U.idRole = R.id";
        Utilisateur user = null;
        try {
            this.db.initPrepare(request);
            this.db.getPstm().setString(1, email);
            this.rs = this.db.executeSelect();
            if (this.rs.next()) {
                String motDePasse1 = null;
                String motDePasse2 = null;

                boolean changed = false; // Permet de verifier si le mot de passe a été changé
                String motDePasseDecrypte = this.rs.getString("U.motDePasse");


                if (this.checkMotDePasse("passer", motDePasseDecrypte)) {
                    changed = true;
                    this.systemPause();
                    this.systemCls();
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
                if(changed)
                {
                    user.setMotDePasse(motDePasse1);
                    user.setMotDePasseHache(this.crypteMotDePasse(motDePasse1));
                }else
                {
                    user.setMotDePasse(motDePasseSaisi);
                    user.setMotDePasseHache(this.rs.getString("U.motDePasse"));
                }

                user.setRole(this.rs.getString("R.nom"));

                this.modifierMotDePasse(user.getMotDePasseHache(), user.getId());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

    /**
     * Represente un menu de manière général. En fonction de l'utilisateur
     * qui se connecte, il est dirigé vers un menu bien determiné.
     *
     * @param user L'utilsateur qui s'est connecté
     */
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

    /**
     * Le menu des tout autre utilisateurs en dehors de l'admin et du RH.
     */
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


    /**
     * Modifie un mot de passe.
     *
     * @param motDePasse Le nouveau mot de passe
     *
     * @param id L'identifiant de l'utilisateur dont le mot de passe doit être modifié
     */
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

    /**
     * "Nettoie" la console.
     */
    public void systemCls() {


        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Met le programme en pause jusqu'à ce aue l'utilisateur presse une touche.
     */
    public void systemPause() {


        System.out.print(" ".repeat(60) + "Appuyez sur une touche pour continuer...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Crypte ou decrypte un mot de passe passer en paramètre.
     *
     * @param motDePasse  Le mot de passe à crypter ou decrypter
     *
     * @return Le mot de passe crypter  ou decrypter
     */
    public String crypteMotDePasse(String motDePasse)
    {
        String sel = BCrypt.gensalt();
        return BCrypt.hashpw(motDePasse, sel);
    }

    /**
     * Verifie que les deux mots de passe correspondent.
     * @param motDePasse1 Le mot de passe à verifier
     * @param motDePasse2 Le mot de passe stocké
     * @return true s'il y a correspondance, sinon false
     */
    public boolean checkMotDePasse(String motDePasse1, String motDePasse2)
    {
        return BCrypt.checkpw(motDePasse1, motDePasse2);
    }

    /**
     * Convertit la liste des utilisateurs en une liste de liste de chaines de caracteres.
     * La liste externe constitue tous les utilisateurs et la liste interne constitue un
     * utilisateur. Cahque informations d'un utilisateur est chaine de caractere.
     * Cela facilite la creation du tableau.
     * @param users La liste des utilisateurs
     * @return La matrice contenant les utilisateurs
     */
    public List<List<String>> convertir(List<Utilisateur> users)
    {
        List<List<String>> allUsers = new ArrayList<>();

        List<String> titres = new ArrayList<>();
        titres.add("#");
        titres.add("Nom");
        titres.add("Prenom");
        titres.add("Email");
        titres.add("Téléphone");
        titres.add("Adresse");
        titres.add("Mot de passe");
//        titres.add("Mot de passe crypté");
        titres.add("Rôle");

        allUsers.add(titres);

        for(Utilisateur user : users)
        {
            List<String> utilisateur = new ArrayList<>();
            utilisateur.add(String.valueOf(user.getId()));
            utilisateur.add(user.getNom());
            utilisateur.add(user.getPrenom());
            utilisateur.add(user.getEmail());
            utilisateur.add(user.getTelephone());
            utilisateur.add(user.getAdresse());
//            utilisateur.add(user.getMotDePasse());
            utilisateur.add(user.getMotDePasseHache());
            utilisateur.add(user.getRole());
            allUsers.add(utilisateur);
        }
        return allUsers;
    }


    /**
     * Crée un tableau en console qui liste les differents utilisateurs.
     * @param tableauUtilisateur Le tableau des utilisateurs
     */
    public void creerTableau(List<List<String>> tableauUtilisateur)
    {
        List<Integer> taillesTableaux = new ArrayList<>();

        /**
         * Determiner pour chaque colonne la longueur du mot le plus long
         * et l'insere dans un tableau.
         */
        int tailleMax = 0 ;
        for (int i = 0; i < tableauUtilisateur.get(0).size(); i++) {
            tailleMax = 0;
            for (int j = 0; j < tableauUtilisateur.size(); j++) {
                if(tableauUtilisateur.get(j).get(i).length() > tailleMax)
                {
                    tailleMax = tableauUtilisateur.get(j).get(i).length();

                }
            }
            taillesTableaux.add(tailleMax);
        }

        /** Fait la somme du tableau des differentes tailles dans le but
           de savoir combien de fois l'on doit afficher ce caractere "-"
           Cela permet de separer certaines lines du tableau.
         */
        int somme = 0;
        for(int num : taillesTableaux)
        {
            somme += num;
        }

        System.out.println("-".repeat(somme + 3*taillesTableaux.size() + 1));

        /**
         * Crée les differentes lignes et colonnes.
         * | : Une barre verticale servant de séparateur dans le tableau.
         * % : Marque le début d'une spécification de format.
         * - : Indique un alignement à gauche de la valeur.
         * + (longueursMax[j] + 1) : Cela représente la largeur de la
         * colonne pour la valeur en cours (la longueur maximale de la
         * colonne, plus 1 pour un espace supplémentaire).
         */
        boolean isFirstLine = true;
        for (List<String> ligne : tableauUtilisateur) {
            for (int j = 0; j < ligne.size(); j++) {
                String format = "| %-"+ (taillesTableaux.get(j) + 1) + "s";
                System.out.printf(format, ligne.get(j));

            }
            System.out.println("|");

            /*Permet de savoir si lors du listing, on a à faire à aux titres ou pas.
              Cela permet de séparer les titres des autres lignes
            */
            if(isFirstLine)
            {
                System.out.println("-".repeat(somme + 3*taillesTableaux.size() + 1));
                isFirstLine = false;
            }

        }
        System.out.println("-".repeat(somme + 3*taillesTableaux.size() + 1));
    }
}




