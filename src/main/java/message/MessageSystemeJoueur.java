package message;

import joueur.Deplacement;

import java.io.Serializable;

public class MessageSystemeJoueur implements Serializable {
	public enum TypeMessage {
    	MAJ_CARTE,
    	LIBERE,
    	CHANGMT_ZONE,
    	VALIDE
    }

	private TypeMessage type;
    private int[][] nvelleCarte;
    private String nvelleZone;
    private Deplacement directionDepl;
	
    /**
     * Permet d'envoyer la nouvelle carte visible par le joueur
     * @param nvelleCarte zone visible par le joueur
     */
	public MessageSystemeJoueur(int[][] nvelleCarte) {
        type = TypeMessage.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
    }

	/**
	 * Indique un changement de zone pour le joueur
     * @param nvelleZone nom de la zone à laquelle se connecter
     */
    public MessageSystemeJoueur(String nvelleZone) {
        type = TypeMessage.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
    }
    
    /**
     * Valide le déplacement du joueur
     * @param directionDepl direction validée
     */
    public MessageSystemeJoueur(Deplacement directionDepl) {
    	type = TypeMessage.VALIDE;
    	this.directionDepl = directionDepl;
    }
    
    /**
     * Valide la déconnexion du joueur
     */
    public MessageSystemeJoueur(){
        type = TypeMessage.LIBERE;
    }
    
    /**
     * Retourne le type du message
     * @return type du message
     */
    public TypeMessage getType() {
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
     * Retourne la nouvelle zone
     * @return nvlleZone la nouvelle zone
     */
    public String getNvelleZone() {
        return nvelleZone;
    }   
    
    /**
     * Retourne le déplacement du joueur valid�
     * @return directionDepl le déplacement du joueur
     */
    public Deplacement getDirectionDepl() {
        return directionDepl;
    }
   
}
