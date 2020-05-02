package message;

import java.io.Serializable;

public class MessageSysteme implements Serializable {
    private String type;
    private int[] indicesCase;
    private int[][] voisinsCase;

    public MessageSysteme(int xCase, int yCase) {
        type = "Demande";
        indicesCase = new int[]{xCase, yCase};
    }

    public MessageSysteme(int[][] voisins) {
        type = "Reponse";
        voisinsCase = voisins;
    }

    public String getType() { return type; }

    public int[][] getVoisinsCase() { return voisinsCase; }

}
