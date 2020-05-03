package message;

import java.io.Serializable;

import types.Deplacement;
import types.MessageSystemeToJoueur;

import java.io.Serializable;
public class MessageSystemeJoueur implements Serializable {
	private MessageSystemeToJoueur type;
    private int[][] nvelleCarte;
    private String nvelleZone;
    private Deplacement directionDepl;
    private boolean erreur = false;
	
    /**
     * Permet d'envoyer la nouvelle carte visible par le joueur
     * @param nvelleCarte zone visible par le joueur
     */
	public MessageSystemeJoueur(int[][] nvelleCarte) {
        type = MessageSystemeToJoueur.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
    }

	/**
	 * Indique un changement de zone pour le joueur
     * @param nvelleZone nom de la zone à laquelle se connecter
     */
    public MessageSystemeJoueur(String nvelleZone) {
        type = MessageSystemeToJoueur.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
    }
    
    public MessageSystemeJoueur(boolean erreur) {
    	this.erreur = erreur;
    	if(erreur == true) {
    		type = MessageSystemeToJoueur.ERREUR;
    	}
    }
    
    public MessageSystemeJoueur() {
    	type = MessageSystemeToJoueur.INIT;
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
    public Deplacement getDirectionDepl() {
        return directionDepl;
    }
   
}
