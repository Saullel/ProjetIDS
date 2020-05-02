package message;

import java.io.Serializable;

import message.MessageSystemeJoueur.TypeMessage;

public class MessageSystemeSysteme implements Serializable {
	public enum TypeMessage {
    	DEMANDE,
    	REPONSE
    }
	
	private TypeMessage type;
    private int[] indicesCase;
    private int[][] voisinsCase;

    /**
     * Initialise les coordonn�es d'une case suite � une demande
     * @param xCase coordonn�e en x de la case
     * @param yCase coordonn�e en y de la case
     */
    public MessageSystemeSysteme(int xCase, int yCase) {
        type = TypeMessage.DEMANDE;
        indicesCase = new int[]{xCase, yCase};
    }

    /**
     * Initialise la r�ponse � une demande de voisins d'une case
     * @param voisins voisins d'une case
     */
    public MessageSystemeSysteme(int[][] voisins) {
        type = TypeMessage.REPONSE;
        voisinsCase = voisins;
    }

    /**
     * Retourne le type du message
     * @return type du message
     */
    public TypeMessage getType() {
    	return type;
    }

    /**
     * Retourne les voisins d'une case
     * @return voisinsCase les voisins d'une case
     */
    public int[][] getVoisinsCase() {
    	return voisinsCase;
    }

}
