package message;

import joueur.DescriptionJoueur;
import types.Deplacement;
import types.MessageJoueurToSysteme;

import java.io.Serializable;

// TODO : actualiser param
public class MessageJoueurSysteme implements Serializable {
    private MessageJoueurToSysteme type;
	private String queueReponse;
	private int id;
    private Deplacement directionDepl;
    private DescriptionJoueur descriptionJoueur;
    private int emplacementX;
    private int emplacementY;

    /***
     * Initialise la communication entre le joueur et le systeme
     * //@param dj description du joueur
     * @param x emplacement du joueur sur l'axe x
     * @param y emplacement du joueur sur l'axe y
     */
    public MessageJoueurSysteme(int x, int y) {
        type = MessageJoueurToSysteme.INIT;
        //descriptionJoueur = dj;
        emplacementX = x;
        emplacementY = y;
    }

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
     * Initialise la description du joueur
     * //@param queueReponse queue sur laquelle le systeme doit répondre
     * @param id identifiant du joueur dans la zone
     * //@param descriptionJoueur les caractéristiques du joueur
     */
	/*public MessageJoueurSysteme(int id) {
		type = MessageJoueurToSysteme.MODIF_INFOS;
		this.id = id;
        //this.queueReponse = queueReponse;
        //this.descriptionJoueur = descriptionJoueur;
    }*/

    /**
     * Demande à quitter le jeu
     * @param id identifiant du joueur dans la zone
     * //@param queueReponse queue sur laquelle le systeme doit répondre
     */
    /*public MessageJoueurSysteme(int id){
        type = MessageJoueurToSysteme.QUITTE;
        this.id = id;
        //this.queueReponse = queueReponse;
    }*/

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

    /**
     * Retourne l'identifiant du joueur
     * @return id l'identifiant du joueur
     */
    public int getId() { return id; }

    /**
     * Retoune l'emplacement en x du joueur
     * @return emplacementX la position du joueur en x
     */
    public int getEmplacementX() { return emplacementX; }

    /***
     * Retourne l'emplacemnt en y du joueur
     * @return emplacementY la position en joueur en y
     */
    public int getEmplacementY() { return emplacementY; }
}
