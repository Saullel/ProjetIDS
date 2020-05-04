package message;

import java.io.Serializable;

import types.MessageSystemeToSysteme;

public class MessageSystemeSysteme implements Serializable {
	private MessageSystemeToSysteme type;
    private String queueReponse;
    private int[] indicesCase;
    private int[][] voisinsCase;

    /**
     * Initialise les coordonnées d'une case suite à une demande
     * @param xCase coordonnées en x de la case
     * @param yCase coordonnées en y de la case
     */
    public MessageSystemeSysteme(String queueReponse, int xCase, int yCase) {
        type = MessageSystemeToSysteme.DEMANDE;
        this.queueReponse = queueReponse;
        indicesCase = new int[]{xCase, yCase};
    }

    /**
     * Initialise la réponse à une demande de voisins d'une case
     * @param voisins voisins d'une case
     */
    public MessageSystemeSysteme(int[][] voisins) {
        type = MessageSystemeToSysteme.REPONSE;
        voisinsCase = voisins;
    }

    /**
     * Retourne le type du message
     * @return type du message
     */
    public MessageSystemeToSysteme getType() {
    	return type;
    }

    /**
     * Retourne les voisins d'une case
     * @return voisinsCase les voisins d'une case
     */
    public int[][] getVoisinsCase() {
    	return voisinsCase;
    }


    public String getQueueReponse() { return queueReponse; }

}
