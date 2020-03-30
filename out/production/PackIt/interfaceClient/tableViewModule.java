package interfaceClient;

public class tableViewModule {
    private   String nom;
    private   int Id;
    private String prix;
    public tableViewModule(int id, String nom, String prix){
        this.Id=id;
        this.nom=nom;
        this.prix=prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrix() {
        return prix;
    }

    public int getId() {
        return Id;
    }
}
