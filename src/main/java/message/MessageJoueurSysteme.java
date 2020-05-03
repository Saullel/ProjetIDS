package message;

import joueur.DescriptionJoueur;
import types.Deplacement;
import types.MessageJoueurToSysteme;

import java.io.Serializable;

public class MessageJoueurSysteme implements Serializable {
    private MessageJoueurToSysteme type;
    private Deplacement directionDepl;
    private DescriptionJoueur descriptionJoueur;

    /**
     * Initialise le déplacement du joueur
     * @param directionDepl direction du déplacement voulu
     */
    public MessageJoueurSysteme(Deplacement directionDepl) {
        type = MessageJoueurToSysteme.DEPLACEMENT;
        this.directionDepl = directionDepl;
    }

    /**
     * Initialise la description du joueur
     * @param descriptionJoueur les caractéristiques du joueur
     */
    public MessageJoueurSysteme(DescriptionJoueur descriptionJoueur) {
        type = MessageJoueurToSysteme.MODIF_INFOS;
        this.descriptionJoueur = descriptionJoueur;
    }

    /**
     * Demande à quitter le jeu
     */
    public MessageJoueurSysteme(){
        type = MessageJoueurToSysteme.QUITTE;
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

}
