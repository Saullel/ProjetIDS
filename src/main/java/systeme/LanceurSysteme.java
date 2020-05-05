package systeme;

import org.apache.log4j.BasicConfigurator;

public class LanceurSysteme {
    public static void main(String[] args){
        try {
            int[][] partieTerrain = new int[][]
                    {{-1,-1,-1,-1,-1}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}};
            int[][] partieTerrain2 = new int[][]
                    {{-1,-1,-1,-1,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}};
            int[][] partieTerrain3 = new int[][]
                    {{-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0},{-1,-1,-1,-1,-1}};
            int[][] partieTerrain4 = new int[][]
                    {{0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1},{-1,-1,-1,-1,-1}};
            BasicConfigurator.configure();
            Systeme HG = new Systeme("HG", partieTerrain){};
            Systeme HD = new Systeme("HD", partieTerrain2){};
            Systeme BG = new Systeme("BG", partieTerrain3){};
            Systeme BD = new Systeme("BD", partieTerrain4){};
            HG.lancement();
            HD.lancement();
            BG.lancement();
            BD.lancement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
