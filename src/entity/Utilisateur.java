package entity;

public class Utilisateur
{
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;

    private String motDePasse;
    private String motDePasseHache;
    private Role role;

    public Utilisateur()
    {
    }

    public Utilisateur(int id, String nom, String prenom, String email, String telephone, String adresse, String motDePasse, String motDePasseHache, Role role)
    {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = this.generateEmail();
        this.telephone = telephone;
        this.adresse = adresse;
        this.motDePasse = motDePasse;
        this.motDePasseHache = null;
        this.role = role;
    }

    private String generateEmail()
    {
        return this.prenom+"@gmail.com";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getMotDePasseHache() {
        return motDePasseHache;
    }

    public void setMotDePasseHache(String motDePasseHache) {
        this.motDePasseHache = motDePasseHache;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
