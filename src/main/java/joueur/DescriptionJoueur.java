package joueur;

import java.io.Serializable;

public class DescriptionJoueur implements Serializable {
    private String nom;
    private String forme;
    private String couleur;

    public DescriptionJoueur(String nom, String forme, String couleur) {
        this.nom = nom;
        this.forme = forme;
        this.couleur = couleur;
    }

    public String getNom() { return nom; }

    public String getForme() { return forme; }

    public String getCouleur() { return couleur; }

    public void setNom(String nom) { this.nom = nom; }

    public void setForme(String forme) { this.forme = forme; }

    public void setCouleur(String couleur) { this.couleur = couleur; }

}
