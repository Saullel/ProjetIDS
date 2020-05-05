package joueur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import types.*;

public class LanceurJoueur {
    public static void main(String[] args){
        try {
            Random r = new Random();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Veuillez entrer un identifian (entier) : ");
            String nom = buffer.readLine();

            Joueur j = new Joueur(nom,r.nextInt(3));
            j.connexion();

            String s;
            
            do {
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
                        System.out.println("Déplacement invalide");
                        break;
                }
            } while(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
