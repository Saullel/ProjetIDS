package systeme;

import org.apache.log4j.BasicConfigurator;

public class LanceurSysteme {
    public static void main(String[] args){
        try {
            int[][] partieTerrain = new int[][]
                    {{-1,-1,-1,-1,-1}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}};
            BasicConfigurator.configure();
            Zone HG = new Zone("HG",partieTerrain){};
            HG.lancement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
