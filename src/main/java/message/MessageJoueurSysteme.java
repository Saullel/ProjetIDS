package message;

import joueur.Deplacements;
import joueur.DescriptionJoueur;

import java.io.Serializable;

public class MessageJoueurSysteme implements Serializable {
	public enum TypeMessage {
        DEPLACEMENT,
        MODIF_INFOS,
        QUITTE,
        VALIDE
    }

    private TypeMessage type;
    private Deplacements directionDepl;
    private DescriptionJoueur descriptionJoueur;

    /**
     * Initialise le déplacement du joueur
     * @param directionDepl direction du déplacement voulu
     */
    public MessageJoueurSysteme(Deplacements directionDepl) {
        type = TypeMessage.DEPLACEMENT;
        this.directionDepl = directionDepl;
    }

    /**
     * Initialise la description du joueur
     * @param descriptionJoueur les caractéristiques du joueur
     */
    public MessageJoueurSysteme(DescriptionJoueur descriptionJoueur) {
        type = TypeMessage.MODIF_INFOS;
        this.descriptionJoueur = descriptionJoueur;
    }

    /**
     * Demande à quitter le jeu
     */
    public MessageJoueurSysteme(){
        type = TypeMessage.QUITTE;
    }

    /**
     * Retourne le type du message
     * @return type du message
     */
    public TypeMessage getType() {
        return type;
    }

    /**
     * Retourne le déplacement du joueur
     * @return directionDepl le déplacement du joueur
     */
    public Deplacements getDirectionDepl() {
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
