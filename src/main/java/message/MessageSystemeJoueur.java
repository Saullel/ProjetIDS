package message;

import joueur.Deplacements;

public class MessageSystemeJoueur {
	public enum TypeMessage {
    	MAJ_CARTE,
    	LIBERE,
    	CHANGMT_ZONE,
    	VALIDE
    }

	private TypeMessage type;
    private int[][] nvelleCarte;
    private String nvelleZone;
    private Deplacements directionDepl;
	
    /**
     * Initialise la nouvelle carte
     * @param nvelleCarte
     */
	public MessageSystemeJoueur(int[][] nvelleCarte) {
        type = TypeMessage.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
    }

	/**
	 * Initialise la nouvelle zone
     * @param nvelleZone
     */
    public MessageSystemeJoueur(String nvelleZone) {
        type = TypeMessage.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
    }
    
    /**
     * Valide le déplacement du joueur
     * @param directionDepl
     */
    public MessageSystemeJoueur(Deplacements directionDepl) {
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
    public Deplacements getDirectionDepl() {
        return directionDepl;
    }
   
}
