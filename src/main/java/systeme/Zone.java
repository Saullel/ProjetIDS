package systeme;

import joueur.DescriptionJoueur;
import org.apache.cassandra.net.Message;
import types.Deplacement;
import types.MessageJoueurToSysteme;
import com.rabbitmq.client.*;
import message.*;
import outils.Envoie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.*;

public class Zone {
    private String nom;
    private int[][] terrain;
    private List<Integer> terrain2;
    private int hauteur;
    private int largeur;
    private Channel channelSysteme;
    private Channel channelJoueur;
    private HashMap<String, String> queueSysteme;
    private HashMap<Integer,String> queueJoueur;
    private List<Integer> joueur;
    private HashMap<Integer, DescriptionJoueur> infoJoueur;
    private int cpt;
    int idJoueur;
    private static final Object o = new Object();

    /**
     * Initialise une zone du terrain
     * @param nom le nom de la zone
     * @param terrain le terrain où la zone est
     */
    public Zone(String nom, int[][] terrain) {
        this.nom = nom;
        this.terrain = terrain;
        hauteur = terrain.length;
        largeur = terrain[0].length;
        queueSysteme = new HashMap<>();
        queueJoueur = new HashMap<>();
        infoJoueur = new HashMap<>();
        joueur= new ArrayList<>();
        cpt = 1;
        idJoueur = 0;
    }

    /**
     * Lancement d'une partie
     */
    public void lancement() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try {
            Connection connection = factory.newConnection();
            channelSysteme = connection.createChannel();
            channelJoueur = connection.createChannel();

            String queue1;
            String queue2;
            String queue3;

            String queue1_inverse;
            String queue2_inverse;
            String queue3_inverse;

            switch (nom) {
                case "HG":
                    queue1 = "HG_to_HD";
                    queue2 = "HG_to_BG";
                    queue3 = "HG_to_BD";

                    queue1_inverse = "HD_to_HG";
                    queue2_inverse = "BG_to_HG";
                    queue3_inverse = "BD_to_HG";

                    queueSysteme.put("aDroite",queue1);
                    queueSysteme.put("enBas",queue2);
                    queueSysteme.put("enDiagonale",queue3);

                    break;
                case "HD":
                    queue1 = "HD_to_HG";
                    queue2 = "HD_to_BD";
                    queue3 = "HD_to_BG";

                    queue1_inverse = "HG_to_HD";
                    queue2_inverse = "BD_to_HD";
                    queue3_inverse = "BG_to_HD";

                    queueSysteme.put("aGauche",queue1);
                    queueSysteme.put("enBas",queue2);
                    queueSysteme.put("enDiagonale",queue3);

                    break;
                case "BG":
                    queue1 = "BG_to_HG";
                    queue2 = "BG_to_BD";
                    queue3 = "BG_to_HD";

                    queue1_inverse = "HG_to_BG";
                    queue2_inverse = "BD_to_BG";
                    queue3_inverse = "HD_to_BG";

                    queueSysteme.put("enHaut",queue1);
                    queueSysteme.put("aDroite",queue2);
                    queueSysteme.put("enDiagonale",queue3);

                    break;
                case "BD":
                    queue1 = "BD_to_BG";
                    queue2 = "BD_to_HD";
                    queue3 = "BD_to_HG";

                    queue1_inverse = "BG_to_BD";
                    queue2_inverse = "HD_to_BD";
                    queue3_inverse = "HG_to_BD";

                    queueSysteme.put("enHaut",queue1);
                    queueSysteme.put("aGauche",queue2);
                    queueSysteme.put("enDiagonale",queue3);

                    break;
                default:
                    queue1 = "";
                    queue2 = "";
                    queue3 = "";

                    queue1_inverse = "";
                    queue2_inverse = "";
                    queue3_inverse = "";
                    break;
            }

            channelSysteme.queueDeclare(queue1, false, false, false, null);
            channelSysteme.queueDeclare(queue2, false, false, false, null);
            channelSysteme.queueDeclare(queue3, false, false, false, null);

            channelSysteme.queueDeclare(queue1_inverse, false, false, false, null);
            channelSysteme.queueDeclare(queue2_inverse, false, false, false, null);
            channelSysteme.queueDeclare(queue3_inverse, false, false, false, null);

            // gestion des messages entre la zone et les joueurs
            DeliverCallback deliverCallbackJoueur = (consumerTag, delivery) -> {
                try {

                    out.println(nom);
                    MessageJoueurSysteme messageJoueurSysteme = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                    int id = messageJoueurSysteme.getId();

                    switch (messageJoueurSysteme.getType()) {
                        case QUITTE:
                            out.println("un joueur souhaite partir");
                            //todo : modifier queueJoueur pour permettre une suppression et un envoie facile (ajout fonction)
                            //String queueSuppr = queueJoueur.get(id);
                            queueJoueur.remove(id);
                            joueur.remove(id);
                            //todo : message aux joueurs indiquant que le joueur courant quitte la zone
                            break;
                        case DEPLACEMENT:
                            deplacerJoueur(id, messageJoueurSysteme.getDirectionDepl());
                            afficherTerrain();
                            break;
                        default:
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };

            DeliverCallback deliverCallbackSysteme = (consumerTag, delivery) -> {
                try {
                    MessageSystemeSysteme mss = (MessageSystemeSysteme) Envoie.deserialize(delivery.getBody());
                    String queue = mss.getQueueReponse();
                    switch (mss.getType()) {
                        case DEMANDE:
                            System.out.println("le systeme veut une réponse");
                            try {
                                int id = getId();
                                int x = mss.getxCase();
                                int y = mss.getyCase();

                                ajouterJoueur(id,x,y);
                                joueur.add(id);
                                afficherTerrain();
                                String nvelleQueue_Envoie = id + "_StoJ";
                                queueJoueur.put(id, nvelleQueue_Envoie);
                                channelJoueur.basicConsume(nvelleQueue_Envoie, true, deliverCallbackJoueur, consumerTag2 -> { });

                                MessageSystemeSysteme reponse = new MessageSystemeSysteme(true,id);
                                channelSysteme.basicPublish("", queue, null, Envoie.serialize(reponse));


                                MessageSystemeJoueur message = new MessageSystemeJoueur(terrain,"Bienvenue");
                                channelJoueur.basicPublish("", nvelleQueue_Envoie, null, Envoie.serialize(message));
                                out.println("x : " + x + ", y : " + y);
                            } catch (Exception e) {
                                MessageSystemeSysteme reponse = new MessageSystemeSysteme(false, -1);
                                channelSysteme.basicPublish("", queue, null, Envoie.serialize(reponse));
                            }
                            break;
                        case REPONSE:
                            if(mss.estSucces()){
                                // todo : ne plus ecouter sur la queue
                                int id = mss.getId();
                                int i = 0;
                                while(i < joueur.size() && joueur.get(i) != id){
                                    i++;
                                }
                                joueur.remove(i);
                                queueJoueur.remove(id);

                                //MessageSystemeJoueur msj = new MessageSystemeJoueur(mss.getId(),mss.getQueueReponse(),false);
                            }
                            else {
                                MessageSystemeJoueur msj = new MessageSystemeJoueur(terrain,"Il semblerait que quelque chose vous bloque :x\n");
                                out.println(queueJoueur);
                                channelJoueur.basicPublish("", queueJoueur.get(mss.getId()), null, Envoie.serialize(mss));
                            }
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            channelSysteme.basicConsume(queue1_inverse, true, deliverCallbackSysteme, consumerTag -> { });
            channelSysteme.basicConsume(queue2_inverse, true, deliverCallbackSysteme, consumerTag -> { });
            channelSysteme.basicConsume(queue3_inverse, true, deliverCallbackSysteme, consumerTag -> { });


            // les queues "connexion" servent à rediriger les joueurs vers leurs propres moyens de communicaton
            String queueConnexion_Envoie = nom + "_Connexion_StoJ";
            String queueConnexion_Reception = nom + "_Connexion_JtoS";
            channelJoueur.queueDeclare(queueConnexion_Envoie, false, false, false, null);
            channelJoueur.queueDeclare(queueConnexion_Reception, false, false, false, null);

            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                try {
                        MessageJoueurSysteme messageJoueurSysteme = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                        if (messageJoueurSysteme.getType() == MessageJoueurToSysteme.INIT) {

                            // mise en place des nouvelles queues
                            int id = messageJoueurSysteme.getId();
                            joueur.add(id);
                            //todo : ajouter message à tous les joueurs indiquant un nouveau venu sur la zone

                            String s = Integer.toString(id);

                            String nvelleQueue_Envoie = s + "_StoJ";
                            String nvelleQueue_Reception = s + "_JtoS";

                            out.println(nvelleQueue_Envoie + " " + nvelleQueue_Reception);

                            channelJoueur.queueDeclare(nvelleQueue_Envoie, false, false, false, null);
                            channelJoueur.queueDeclare(nvelleQueue_Reception, false, false, false, null);

                            queueJoueur.put(id, nvelleQueue_Envoie);

                            channelJoueur.basicConsume(nvelleQueue_Reception, true, deliverCallbackJoueur, consumerTag2 -> { });

                            try {
                                ajouterJoueur(id,-1,-1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // Redirection du joueur vers ses propres queues (envoie & reception)
                            MessageSystemeJoueur message = new MessageSystemeJoueur(terrain,"Bienvenue");
                            channelJoueur.basicPublish("", queueConnexion_Envoie, null, Envoie.serialize(message));
                            afficherTerrain();
                        }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            channelJoueur.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });

            out.println("Zone " + nom + " active");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private synchronized int getId() throws IOException {
        int id = cpt;
        cpt++;
        return id;
    }

    private synchronized void deplacerJoueur(int id, Deplacement d) throws IOException {
        int[] coord = trouverJoueur(id);
        int x = coord[0];
        int y = coord[1];

        switch (d) {
            case HAUT:
                x--;
                break;
            case BAS:
                x++;
                break;
            case GAUCHE:
                y--;
                break;
            case DROIT:
                y++;
        }

        if(x > 0 && x < hauteur && y > 0 && y < largeur) { // dans le terrain
            if(terrain[x][y] == 0){
                supprimerJoueur(id, false);
                try {
                    ajouterJoueur(id, x, y);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                String message;
                if(terrain[x][y] > 0) {
                    message = "Ce n'est pas poli de pousser les gens...\n";
                }
                else {
                    message = "Aïe ! Les murs font mal...\n";
                }
                MessageSystemeJoueur mss = new MessageSystemeJoueur(terrain, message);
                channelJoueur.basicPublish("", queueJoueur.get(id), null, Envoie.serialize(mss));
            }
        }
        else{
            String nomZoneDistante;
            if(x < 0) {
                x = hauteur-1;
                nomZoneDistante = "enHaut";
                //MessageSystemeJoueur mss = new MessageSystemeJoueur(queueSysteme.get("enHaut"),x,y);
            }
            else if (x > hauteur) {
                x = 0;
                nomZoneDistante = "enBas";
                //MessageSystemeJoueur mss = new MessageSystemeJoueur(queueSysteme.get("enBas"),x,y);
            }
            else if (y < 0) {

                y = largeur-1;
                nomZoneDistante = "aGauche";
                //MessageSystemeJoueur mss = new MessageSystemeJoueur(queueSysteme.get("aGauche"),x,y);
            }
            else {
                y = 0;
                nomZoneDistante = "aDroite";
                //MessageSystemeJoueur mss = new MessageSystemeJoueur(queueSysteme.get("aDroite"),x,y);
            }
            MessageSystemeSysteme mss = new MessageSystemeSysteme("HD_to_HG",id,x,y);
            channelSysteme.basicPublish("", "HG_to_HD", null, Envoie.serialize(mss));
        }
    }

    private int[] trouverJoueur(int id) {
        int[] coord = new int[2];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if(terrain[i][j] == id){
                    coord[0] = i;
                    coord[1] = j;
                }
            }
        }
        return coord;
    }

    private synchronized void ajouterJoueur(int id, int x, int y) throws Exception {
        if(x == -1 && y == -1) {
            boolean emplacementLibre = false;
            for (int i = 0; i < hauteur && !emplacementLibre; i++) {
                for (int j = 0; j < largeur && !emplacementLibre; j++) {
                    if(terrain[i][j] == 0){
                        x = i;
                        y = j;
                        emplacementLibre = true;
                    }
                }
            }
            if (!emplacementLibre) { // TODO : gérer l'exception
                throw new Exception("Plus de place coco");
            }
        }
        else if (terrain[x][y] != 0){
            if (x == hauteur && y == largeur) { // TODO : gérer l'exception
                throw new Exception("Plus de place coco");
            }
        }
        terrain[x][y] = id;

        // on prévient les voisins

        //StringBuilder messagePourNouveauJ = new StringBuilder();
        for(int idVoisin : joueur) {
            String message = "Tu as le bonjour du nouveau là !";
            MessageSystemeJoueur messageSystemeJoueur = new MessageSystemeJoueur(terrain,message);
            channelJoueur.basicPublish("", queueJoueur.get(idVoisin), null, Envoie.serialize(messageSystemeJoueur));

            //messagePourNouveauJ.append(" > ").append("un joueur te dit bonjour !\n");
        }
    }

    private synchronized void supprimerJoueur(int id, boolean prevenir) throws IOException {
        int[] coord = trouverJoueur(id);
        int x = coord[0];
        int y = coord[1];

        terrain[x][y] = 0;

        // on prévient les voisins
        if(prevenir){
            for(int idVoisin : joueur) {
                if(idVoisin != id){
                    MessageSystemeJoueur m = new MessageSystemeJoueur(terrain,"");
                    channelJoueur.basicPublish("", queueJoueur.get(idVoisin), null, Envoie.serialize(m));
                }
            }
        }
    }

    //todo : ne pas retourner id mais joueur ou forme ou autre
    private int[][] creationCarte(int x, int y) {
        int[][] carte = new int[3][3];
        
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                int xTransp = x-1+i;
                int yTransp = y-1+i;
                
                if (xTransp < 0 || xTransp > hauteur || yTransp < 0 || yTransp > largeur) {
                    // TODO: regarder si quelqu'un dans la zone adjacente
                    // sera appel fonction demanderCaseVide à coté ?
                    carte[i][j] = -2;
                }
                else { carte[i][j] = terrain[xTransp][yTransp]; }
            }
        }
        return carte;
    }

    private void afficherTerrain() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                switch (terrain[i][j]) {
                    case -1:
                        out.print("* | ");
                        break;
                    case 0:
                        out.print("  | ");
                        break;
                    default:
                        out.print("J | ");
                }
            }
            out.println("");
        }
    }

}
