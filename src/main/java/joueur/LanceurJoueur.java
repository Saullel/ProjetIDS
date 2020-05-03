package joueur;

import systeme.Zone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import types.*;

public class LanceurJoueur {
    public static void main(String[] args){
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Pseudo : ");
            String nom = buffer.readLine();

            Joueur j = new Joueur(nom);
            j.connexion();

            String s;
            do{
                System.out.print(" > ");
                s = buffer.readLine();
                switch (s){
                    case "quit":
                        j.quitter();
                        break;
                    case "q":
                        j.deplacer(Deplacement.GAUCHE);
                        break;
                    case "d":
                        j.deplacer(Deplacement.DROIT);
                        break;
                    case "z":
                        j.deplacer(Deplacement.HAUT);
                        break;
                    case "s":
                        j.deplacer(Deplacement.BAS);
                        break;
                    default:
                        System.out.println("Mauvaise commande");
                        break;
                }
            }while(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
