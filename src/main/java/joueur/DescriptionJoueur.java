package joueur;

import java.io.Serializable;
import types.*;

public class DescriptionJoueur implements Serializable {
    private String nom;
    private Forme forme;
    private Couleur couleur;

    public DescriptionJoueur(String nom, Forme forme, Couleur couleur) {
        this.nom = nom;
        this.forme = forme;
        this.couleur = couleur;
    }

    public String getNom() { return nom; }

    public Forme getForme() { return forme; }

    public Couleur getCouleur() { return couleur; }

    public void setNom(String nom) { this.nom = nom; }

    public void setForme(Forme forme) { this.forme = forme; }

    public void setCouleur(Couleur couleur) { this.couleur = couleur; }

}
