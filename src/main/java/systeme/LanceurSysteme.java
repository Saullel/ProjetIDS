package systeme;

import org.apache.log4j.BasicConfigurator;

public class LanceurSysteme {
    public static void main(String[] args){
        try {
            int[][] partieTerrain = new int[][]
                    {{-1,-1,-1,-1,-1}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}};
            int[][] partieTerrain2 = new int[][]
                    {{-1,-1,-1,-1,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}};
            BasicConfigurator.configure();
            Zone HG = new Zone("HG", partieTerrain){};
            Zone HD = new Zone("HD", partieTerrain2){};
            HG.lancement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
