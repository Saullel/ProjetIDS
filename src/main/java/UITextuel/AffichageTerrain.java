package UITextuel;

public class AffichageTerrain {
	/**
	 * Affiche le terrain
	 * @param t le terrain
	 * @return le terrain sous forme textuelle
	 */
	public static String afficher(int[][] t){
        StringBuilder terrain = new StringBuilder();
        int largeur = t[0].length;
        for (int[] ints : t) {
            for (int j = 0; j < largeur; j++) {
                switch (ints[j]) {
                    case -1:
                        terrain.append("* | ");
                        break;
                    case 0:
                        terrain.append("  | ");
                        break;
                    default:
                        terrain.append("J | ");
                }
            }
            terrain.append("\n");
        }
        terrain.append("\n");
        return terrain.toString();
    }
}
