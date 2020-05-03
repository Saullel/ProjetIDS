package message;

import joueur.DescriptionJoueur;
import types.Deplacement;
import types.MessageJoueurToSysteme;

import java.io.Serializable;

public class MessageJoueurSysteme implements Serializable {
    private MessageJoueurToSysteme type;
	private String queueReponse;
    private Deplacement directionDepl;
    private DescriptionJoueur descriptionJoueur;

    /**
     * Initialise le déplacement du joueur
     * @param queueReponse queue sur laquelle le systeme doit répondre
     * @param directionDepl direction du déplacement voulu
     */

    public MessageJoueurSysteme(String queueReponse, Deplacement directionDepl) {
        type = MessageJoueurToSysteme.DEPLACEMENT;
        this.queueReponse = queueReponse;
        this.directionDepl = directionDepl;
    }

    /**
     * Initialise la description du joueur
     * @param queueReponse queue sur laquelle le systeme doit répondre
     * @param descriptionJoueur les caractéristiques du joueur
     */
	public MessageJoueurSysteme(String queueReponse, DescriptionJoueur descriptionJoueur) {
		type = MessageJoueurToSysteme.MODIF_INFOS;
        this.queueReponse = queueReponse;
        this.descriptionJoueur = descriptionJoueur;
    }

    /**
     * Demande à quitter le jeu
     * @param queueReponse queue sur laquelle le systeme doit répondre
     */
public MessageJoueurSysteme(String queueReponse){
        type = MessageJoueurToSysteme.QUITTE;
        this.queueReponse = queueReponse;
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
     * Retourne la description du joueur
     * @return descriptionJoueur la description du joueur
     */
    public DescriptionJoueur getDescriptionJoueur() {
        return descriptionJoueur;
    }

    /***
     * Retourne la queue sur laquelle le système doit répondre
     * @return le nom que la queue de réponse
     */
    public String getQueueReponse(){ return queueReponse; }

}
