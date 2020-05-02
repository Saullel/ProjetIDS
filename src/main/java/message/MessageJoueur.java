package message;

import joueur.DescriptionJoueur;

import java.io.Serializable;

public class MessageJoueur implements Serializable {
        public enum TypeMessage {
        DEPLACEMENT,
        MODIF_INFOS,
        MAJ_CARTE,
        QUITTE,
        CHANGMT_ZONE
    }

    public enum Deplacement {
        HAUT,
        BAS,
        GAUCHE,
        DROIT
    }

    private TypeMessage type;
    private Deplacement directionDepl;
    private DescriptionJoueur descriptionJoueur;
    private int[][] nvelleCarte;
    private String nvelleZone;

    public MessageJoueur(Deplacement directionDepl) {
        type = TypeMessage.DEPLACEMENT;
        this.directionDepl = directionDepl;
    }

    public MessageJoueur(DescriptionJoueur descriptionJoueur) {
        type = TypeMessage.MODIF_INFOS;
        this.descriptionJoueur = descriptionJoueur;
    }

    public MessageJoueur(int[][] nvelleCarte) {
        type = TypeMessage.MAJ_CARTE;
        this.nvelleCarte = nvelleCarte;
    }

    public MessageJoueur(String nvelleZone) {
        type = TypeMessage.CHANGMT_ZONE;
        this.nvelleZone =  nvelleZone;
    }

    public MessageJoueur(){
        type = TypeMessage.QUITTE;
    }

    public TypeMessage getType() {
        return type;
    }

    public Deplacement getDirectionDepl() {
        return directionDepl;
    }

    public DescriptionJoueur getDescriptionJoueur() {
        return descriptionJoueur;
    }

    public int[][] getNvelleCarte() {
        return nvelleCarte;
    }

    public String getNvelleZone() {
        return nvelleZone;
    }
}
