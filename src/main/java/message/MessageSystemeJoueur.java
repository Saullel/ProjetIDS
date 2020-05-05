package message;

import java.io.Serializable;

import types.Deplacement;
import types.MessageSystemeToJoueur;

import java.io.Serializable;

//todo : actualiser javadoc
public class MessageSystemeJoueur implements Serializable {
	private MessageSystemeToJoueur type;
    private int[][] nvelleCarte;
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
     * Retourne le message au joueur
     * @return message le message au joueur
     */
    public String getMessage() { return message; }
   
}
