package Message;

import Types.Deplacement;
import Types.MessageJoueurToSysteme;

import java.io.Serializable;

public class MessageJoueurSysteme implements Serializable {
    private MessageJoueurToSysteme type;
	private int id;
    private Deplacement directionDepl;


    /**
     * Initialise le déplacement du joueur
     * @param id identifiant du joueur
     * @param directionDepl déplacement du joueur
     */
    public MessageJoueurSysteme(int id, Deplacement directionDepl) {
        type = MessageJoueurToSysteme.DEPLACEMENT;
        this.id = id;
        //this.queueReponse = queueReponse;
        this.directionDepl = directionDepl;
    }

    /**
     * Initialise le type du message Joueur
     * @param id l'identifiant du joueur
     * @param quitte un booléen pour savoir si le joueur souhaite quitter le jeu
     */
    public MessageJoueurSysteme(int id, boolean quitte){
        if(quitte) { type = MessageJoueurToSysteme.QUITTE;}
        else {  type = MessageJoueurToSysteme.INIT; }

        this.id = id;
        //this.queueReponse = queueReponse;
    }

    /**
     * Retourne le type du message
     * @return type du message
     */
    public MessageJoueurToSysteme getType() {
        return type;
    }

    /**
     * Retourne le déplacement du joueur
     * @return directionDepl le déplacement du joueur
     */
    public Deplacement getDirectionDepl() {
        return directionDepl;
    }


    /**
     * Retourne l'identifiant du joueur
     * @return id l'identifiant du joueur
     */
    public int getId() { return id; }

}
