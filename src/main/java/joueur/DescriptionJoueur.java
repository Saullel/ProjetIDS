package joueur;

import java.io.Serializable;
import types.*;

public class DescriptionJoueur implements Serializable {
    private String nom;
    /*private Forme forme;
    private Couleur couleur;*/

    /**
     * Initialise le joueur
     * @param nom le nom du joueur
     */
    public DescriptionJoueur(String nom) {
        this.nom = nom;
        /*this.forme = forme;
        this.couleur = couleur;*/
    }

    /**
     * Retourne le nom du joueur
     * @return nom le nom du joueur
     */
    public String getNom() { return nom; }

    //public Forme getForme() { return forme; }

    //public Couleur getCouleur() { return couleur; }

    /**
     * Définit le nom du joueur
     * @param nom le nom du joueur
     */
    public void setNom(String nom) { this.nom = nom; }

    //public void setForme(Forme forme) { this.forme = forme; }

    //public void setCouleur(Couleur couleur) { this.couleur = couleur; }

}
