package message;

import java.io.Serializable;

import types.Deplacement;
import types.MessageSystemeToJoueur;

import java.io.Serializable;

//todo : actualiser javadoc
public class MessageSystemeJoueur implements Serializable {
	private MessageSystemeToJoueur type;
    private int[][] nvelleCarte;
    private String nvelleZone;
    private boolean erreur = false;
    private int id;
    private int nouvelX;
    private int nouvelY;
    private String message;
	
    /**
     * Permet d'envoyer la nouvelle carte visible par le joueur
     * @param nvelleCarte zone visible par le joueur
     */
	public MessageSystemeJoueur(int[][] nvelleCarte, String message) {
        type = MessageSystemeToJoueur.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
        this.message = message;
    }

	/**
	 * Indique un changement de zone pour le joueur
     * @param nvelleZone nom de la zone à laquelle se connecter
     */
    public MessageSystemeJoueur(String nvelleZone, int nouvelX, int nouvelY) {
        type = MessageSystemeToJoueur.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
        this.nouvelX = nouvelX;
        this. nouvelY= nouvelY;
    }

    // todo : utile ?
    public MessageSystemeJoueur(boolean erreur) {
    	this.erreur = erreur;
    	if(erreur == true) {
    		type = MessageSystemeToJoueur.ERREUR;
    	}
    }
    
    public MessageSystemeJoueur(int id, String nomQueue) {
    	type = MessageSystemeToJoueur.INIT;
    	this.id = id;
    	this.message = nomQueue;
    }
    
    /**
     * Retourne le type du message
     * @return type du message
     */
    public MessageSystemeToJoueur getType() {
        return type;
    }

    /**
     * Retourne la nouvelle carte
     * @return nvelleCarte la nouvelle carte
     */
    public int[][] getNvelleCarte() {
        return nvelleCarte;
    }

    /**
     * Retourne la nouvelle zone
     * @return nvlleZone la nouvelle zone
     */
    public String getNvelleZone() {
        return nvelleZone;
    }   
    
    /**
     * Retourne le déplacement du joueur valid�
     * @return directionDepl le déplacement du joueur
     */
    /*public Deplacement getDirectionDepl() {
        return directionDepl;
    }*/

    public int getId() { return id; }

    public int getNouvelX() { return nouvelX; }

    public int getNouvelY() { return nouvelY; }

    public String getMessage() { return message; }

   
}
