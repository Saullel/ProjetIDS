package message;

import java.io.Serializable;

import types.MessageSystemeToSysteme;

public class MessageSystemeSysteme implements Serializable {
	private MessageSystemeToSysteme type;
    private String queueReponse;
    private int xCase, yCase;
    private int id;
    private boolean succes;

    /**
     * Initialise les coordonnées d'une case suite à une demande
     * @param queueReponse la queue sur laquelle s'envoie le message réponse
     * @param id l'identifiant du joueur
     * @param xCase coordonnées en x de la case
     * @param yCase coordonnées en y de la case
     */
    public MessageSystemeSysteme(String queueReponse, int id, int xCase, int yCase) {
        type = MessageSystemeToSysteme.DEMANDE;
        this.queueReponse = queueReponse;
        this.id = id;
        this.xCase = xCase;
        this.yCase = yCase;
    }

    /**
     * Initialise la réponse à une demande de voisins d'une case
     * @param succes un booléen pour information de la réussite ou de l'échec de l'opération
     * @param id l'identifiant du joueur
     */
    public MessageSystemeSysteme(boolean succes, int id) {
        type = MessageSystemeToSysteme.REPONSE;
        this.succes = succes;
        this.id = id;
    }

    /**
     * Retourne le type du message
     * @return type le type du message
     */
    public MessageSystemeToSysteme getType() {
    	return type;
    }

    /**
     * Retourne les réponses depuis la queue
     * @return queueReponse la réponse depuis la queue
     */
    public String getQueueReponse() { return queueReponse; }
    
    /**
     * Renvoie la position en x d'une case
     * @return xCase la position en x d'une case
     */
    public int getxCase() {
        return xCase;
    }

    /**
     * Renvoie la position en y d'une case
     * @return yCase la position en y d'une case
     */
    public int getyCase() {
        return yCase;
    }

    /**
     * Renvoie la valeur du booléen succes
     * @return succes le booléen
     */
    public boolean estSucces() {
        return succes;
    }

    /**
     * Renvoie l'identifiant de
     * @return id l'identifiant
     */
    public int getId() {
        return id;
    }
}
