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
     * Initialise le message au joueur
     * @param nvelleCarte la carte
     * @param message le message envoyé au joueur
     */
	public MessageSystemeJoueur(int[][] nvelleCarte, String message) {
        type = MessageSystemeToJoueur.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
        this.message = message;
    }

	/**
	 * Initialise le message au joueur
	 * @param nvelleZone la carte de la zone
	 * @param nouvelX la potision en X du joueur
	 * @param nouvelY la position en Y du joueur
	 */
    public MessageSystemeJoueur(String nvelleZone, int nouvelX, int nouvelY) {
        type = MessageSystemeToJoueur.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
        this.nouvelX = nouvelX;
        this. nouvelY= nouvelY;
    }

    // TODO : utile ?
    public MessageSystemeJoueur(boolean erreur) {
    	this.erreur = erreur;
    	if(erreur == true) {
    		type = MessageSystemeToJoueur.ERREUR;
    	}
    }
    
    /**
     * Initialise le message au joueur
     * @param id l'identifiant du joueur
     * @param nomQueue la nom de la queue
     */
    public MessageSystemeJoueur(int id, String nomQueue) {
    	type = MessageSystemeToJoueur.INIT;
    	this.id = id;
    	this.message = nomQueue;
    }
    
    /**
     * Retourne le type du message
     * @return type le type du message
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
     * Retourne le déplacement du joueur validé
     * @return directionDepl le déplacement du joueur
     */
    /*public Deplacement getDirectionDepl() {
        return directionDepl;
    }*/

    /**
     * Retourne l'identifiant du joueur
     * @return id l'identifiant du joueur
     */
    public int getId() { return id; }

    /**
     * Retourne la position en X du joueur
     * @return nouvelX la position en X du joueur
     */
    public int getNouvelX() { return nouvelX; }

    /**
     * Retourne la position en Y du joueur
     * @return nouvelY la position en Y du joueur
     */
    public int getNouvelY() { return nouvelY; }

    /**
     * Retourne le message au joueur
     * @return message le message au joueur
     */
    public String getMessage() { return message; }
   
}
