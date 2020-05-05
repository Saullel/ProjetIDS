package systeme;

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

public class Systeme {
    private String nom;
    private int[][] terrain;
    private int hauteur;
    private int largeur;
    private Channel channelSysteme;
    private Channel channelJoueur;
    private HashMap<String, String> queueSysteme;
    private HashMap<Integer,String> queueJoueur;
    private HashMap<Integer, String> consumerTags;
    private List<Integer> joueursPresents;

    /**
     * Initialise une systeme du terrain
     * @param nom le nom de la systeme
     * @param terrain le terrain où la systeme est
     */
    public Systeme(String nom, int[][] terrain) {
        this.nom = nom;
        this.terrain = terrain;
        hauteur = terrain.length;
        largeur = terrain[0].length;
        queueSysteme = new HashMap<>();
        queueJoueur = new HashMap<>();
        consumerTags = new HashMap<>();
        joueursPresents = new ArrayList<>();
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


            // Gestion des messages entre le système et les joueurs
            DeliverCallback deliverCallbackJoueur = (consumerTag, delivery) -> {
                try {
                    MessageJoueurSysteme messageJoueurSysteme = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                    int id = messageJoueurSysteme.getId();
                    switch (messageJoueurSysteme.getType()) {
                        case QUITTE:
                            out.println("un joueur souhaite partir");
                            supprimerJoueur(id,true);
                            queueJoueur.remove(id);
                            joueursPresents.remove(id);
                            break;
                        case DEPLACEMENT:
                            deplacerJoueur(id, messageJoueurSysteme.getDirectionDepl());
                            break;
                        default:
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };

            // Gestion des messages entre les systèmes
            DeliverCallback deliverCallbackSysteme = (consumerTag, delivery) -> {
                try {
                    MessageSystemeSysteme mss = (MessageSystemeSysteme) Envoie.deserialize(delivery.getBody());
                    String queue = mss.getQueueReponse();
                    switch (mss.getType()) {
                        case DEMANDE:
                            try {
                                int id = mss.getId();
                                int x = mss.getxCase();
                                int y = mss.getyCase();

                                ajouterJoueur(id,x,y,true);
                                joueursPresents.add(id);
                                afficherTerrain();
                                String nvelleQueue_Envoie = id + "_StoJ";
                                queueJoueur.put(id, nvelleQueue_Envoie);

                                String currentTag = channelJoueur.basicConsume(id + "_JtoS", true, deliverCallbackJoueur, consumerTag2 -> { });
                                consumerTags.put(id, currentTag);

                                MessageSystemeSysteme reponse = new MessageSystemeSysteme(true,id);
                                channelSysteme.basicPublish("", queue, null, Envoie.serialize(reponse));

                                MessageSystemeJoueur message = new MessageSystemeJoueur(terrain,"Bienvenue");
                                channelJoueur.basicPublish("", nvelleQueue_Envoie, null, Envoie.serialize(message));
                                out.println("x : " + x + ", y : " + y);
                            } catch (Exception e) {
                                MessageSystemeSysteme reponse = new MessageSystemeSysteme(false, mss.getId());
                                channelSysteme.basicPublish("", queue, null, Envoie.serialize(reponse));
                            }
                            break;
                        case REPONSE:
                            if(mss.estSucces()){
                                int id = mss.getId();
                                int i = 0;
                                supprimerJoueur(id,true);
                                while(i < joueursPresents.size() && joueursPresents.get(i) != id){
                                    i++;
                                }
                                joueursPresents.remove(i);
                                queueJoueur.remove(id);

                                System.out.println("id : "+id);
                                channelJoueur.basicCancel(consumerTags.get(id));
                                consumerTags.remove(id);
                            }
                            else {
                                MessageSystemeJoueur msj = new MessageSystemeJoueur(terrain,"Il semblerait que quelque chose vous bloque :x\n");
                                channelJoueur.basicPublish("", queueJoueur.get(mss.getId()), null, Envoie.serialize(msj));
                            }
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
/*
            String HG_recept = channelSysteme.queueDeclare().getQueue();
            String HD_recept = channelSysteme.queueDeclare().getQueue();
            String BG_recept = channelSysteme.queueDeclare().getQueue();
            String BD_recept = channelSysteme.queueDeclare().getQueue();

            queueSysteme.put("HG",HG_recept);
            queueSysteme.put("HD",HG_recept);
            queueSysteme.put("BG",HG_recept);
            queueSysteme.put("BD",HG_recept);*/

            channelSysteme.queueDeclare("HG", false, false, false, null);
            channelSysteme.queueDeclare("HD", false, false, false, null);
            channelSysteme.queueDeclare("BG", false, false, false, null);
            channelSysteme.queueDeclare("BD", false, false, false, null);

            queueSysteme.put("HG","HG");
            queueSysteme.put("HD","HD");
            queueSysteme.put("BG","BG");
            queueSysteme.put("BD","BD");


            switch (nom) {
                case "HG":
                    channelSysteme.basicConsume(queueSysteme.get("HG"), true, deliverCallbackSysteme, consumerTag -> { });
                    break;
                case "HD":
                    channelSysteme.basicConsume(queueSysteme.get("HD"), true, deliverCallbackSysteme, consumerTag -> { });
                    break;
                case "BG":
                    channelSysteme.basicConsume(queueSysteme.get("BG"), true, deliverCallbackSysteme, consumerTag -> { });
                    break;
                case "BD":
                    channelSysteme.basicConsume(queueSysteme.get("BD"), true, deliverCallbackSysteme, consumerTag -> { });
                    break;
                default:

                    break;
            }


            // Les queues "connexion" servent à rediriger les joueurs vers leurs propres moyens de communication
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
                        joueursPresents.add(id);

                        String s = Integer.toString(id);

                        String nvelleQueue_Envoie = s + "_StoJ";
                        String nvelleQueue_Reception = s + "_JtoS";

                        out.println(nvelleQueue_Envoie + " " + nvelleQueue_Reception);

                        channelJoueur.queueDeclare(nvelleQueue_Envoie, false, false, false, null);
                        channelJoueur.queueDeclare(nvelleQueue_Reception, false, false, false, null);

                        queueJoueur.put(id, nvelleQueue_Envoie);

                        String currentTag =  channelJoueur.basicConsume(nvelleQueue_Reception, true, deliverCallbackJoueur, consumerTag2 -> { });
                        consumerTags.put(id, currentTag);
                        try {
                            ajouterJoueur(id, -1, -1, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Redirection du joueur vers ses propres queues (envoie & reception)
                        MessageSystemeJoueur message = new MessageSystemeJoueur(terrain,"Bienvenue");
                        channelJoueur.basicPublish("", queueConnexion_Envoie, null, Envoie.serialize(message));
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            channelJoueur.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });

            out.println("Systeme " + nom + " actif");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gère le déplacement d'un joueur
     * @param id l'identifiant du joueur
     * @param d le déplacement du joueur
     * @throws IOException
     */
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

        // Si le déplacement mène vers une case du terrain
        if(x > -1 && x < hauteur && y > -1 && y < largeur) { // dans le terrain
            if(terrain[x][y] == 0){
                supprimerJoueur(id, false);
                try {
                    ajouterJoueur(id, x, y, false);
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
        else { // sinon trouver le systeme adéquat
            String systAContacter;
            if(x < 0) { // on veut monter
                if(nom.equals("BG")){ systAContacter = "HG";}
                else { systAContacter = "HD";}
                x = hauteur-1;
            }
            else if (x == hauteur) { // on veut descendre
                if(nom.equals("HG")){ systAContacter = "BG";}
                else { systAContacter = "BD";}
                x = 0;
            }
            else if (y < 0) { // on veut aller à gauche
                if(nom.equals("HD")){ systAContacter = "HG";}
                else { systAContacter = "BG";}
                y = largeur-1;
            }
            else { // on veut aller à droite
                if(nom.equals("HG")){ systAContacter = "HD";}
                else { systAContacter = "BD";}
                y = 0;
            }
            MessageSystemeSysteme mss = new MessageSystemeSysteme(queueSysteme.get(nom),id, x, y);
            channelSysteme.basicPublish("", queueSysteme.get(systAContacter), null, Envoie.serialize(mss));

        }
    }

    /**
     * Trouve la position d'un joueur
     * @param id l'identifiant du joueur
     * @return int[] la position du joueur
     */
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

    /**
     * Ajoute un joueur
     * @param id l'identifiant du joueur
     * @param x la position en x
     * @param y la position en y
     * @param init un booléen
     * @throws Exception
     */
    private synchronized void ajouterJoueur(int id, int x, int y, boolean init) throws Exception {
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
            if (!emplacementLibre) {
                throw new Exception("Plus de place coco");
            }
        }
        else if (terrain[x][y] != 0){
            throw new Exception("Plus de place coco");
        }
        terrain[x][y] = id;

        // on prévient les voisins

        String message = "";
        for(int idVoisin : joueursPresents) {
            if(init) {
                if (idVoisin != id) {
                    message = "Tu as le bonjour du nouveau là !";
                } else {
                    message = "Bienvenue :)";
                }
            }
            MessageSystemeJoueur messageSystemeJoueur = new MessageSystemeJoueur(terrain, message);
            channelJoueur.basicPublish("", queueJoueur.get(idVoisin), null, Envoie.serialize(messageSystemeJoueur));
        }
    }

    /**
     * Supprimer un joueur de son emplacement
     * @param id l'identifiant du joueur
     * @param prevenir un booléen
     * @throws IOException
     */
    private synchronized void supprimerJoueur(int id, boolean prevenir) throws IOException {
        int[] coord = trouverJoueur(id);
        int x = coord[0];
        int y = coord[1];

        terrain[x][y] = 0;

        // On prévient les voisins
        if(prevenir){
            for(int idVoisin : joueursPresents) {
                if(idVoisin != id){
                    MessageSystemeJoueur m = new MessageSystemeJoueur(terrain,"");
                    channelJoueur.basicPublish("", queueJoueur.get(idVoisin), null, Envoie.serialize(m));
                }
            }
        }
    }

    /**
     * Affiche le terrain
     */
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