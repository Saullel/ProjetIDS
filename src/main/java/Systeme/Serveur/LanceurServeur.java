package Systeme.Serveur;

import org.apache.log4j.BasicConfigurator;

public class LanceurServeur {
    public static void main(String[] args) {
        try {
            int[][][] terrain = new int[][][]
                    {{{-1,-1,-1,-1,-1}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}},
                    {{-1,-1,-1,-1,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}},
                    {{-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,0,0,0,0}, {-1,-1,-1,-1,-1}},
                    {{0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1}, {0,0,0,0,-1},{-1,-1,-1,-1,-1}}};


            BasicConfigurator.configure();
            ServeurZone s = null;
            if (!args[0].equals("--zone")){
                System.out.println(args[0] + " n'est pas une commande valide");
                System.exit(-1);
            }
            switch (args[1]){
                case "HG":
                    s = new ServeurZone("HG", terrain[0]){};
                    break;
                case "HD":
                    s = new ServeurZone("HD", terrain[1]){};
                    break;
                case "BG":
                    s = new ServeurZone("BG", terrain[2]){};
                    break;
                case "BD":
                    s = new ServeurZone("BD", terrain[3]){};
                    break;
                default:
                    System.out.println("Erreur : les systèmes ont des noms définis : HG, HD, BG, BD");
                    System.exit(1);
                    break;
            }
            s.lancement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
