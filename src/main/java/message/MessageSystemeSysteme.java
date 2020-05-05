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
     *
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

    public int getxCase() {
        return xCase;
    }

    public int getyCase() {
        return yCase;
    }

    public boolean estSucces() {
        return succes;
    }

    public int getId() {
        return id;
    }
}
