package systeme;

import com.rabbitmq.client.*;
import message.*;
import outils.Envoie;

import java.util.HashMap;

public class Zone {
    String nom;
    int[][] partieTerrain;
    //List<Channel> channelSysteme;
    Channel channelJoueur;
    HashMap<Integer,String> queueJoueur_Envoie;
    //HashMap<Integer,String> queueJoueur_Reception;
    int cpt;
    private static final Object o = new Object();

    public Zone(String nom, int[][] partieTerrain) {
        this.nom = nom;
        this.partieTerrain = partieTerrain;
        queueJoueur_Envoie = new HashMap<>();
        //queueJoueur_Reception = new HashMap<>();
        cpt = 0;
    }
    public void lancement(){

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection =  factory.newConnection();
            channelJoueur = connection.createChannel();

            // gestion des messages entre la zone et les joueurs
            DeliverCallback deliverCallbackJoueur = (consumerTag, delivery) -> {
                try {
                    MessageJoueurSysteme mjs = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                    String queue = mjs.getQueueReponse();
                    MessageSystemeJoueur msj;
                    switch (mjs.getType()) {
                        case QUITTE:
                            System.out.println("un joueur souhaite partir");
                            msj = new MessageSystemeJoueur();
                            break;
                        case DEPLACEMENT:
                            System.out.println("déplacement vers " + mjs.getDirectionDepl());
                            int[][] nvelleZone = new int[][]{{0,0,0},{0,0,0},{0,0,0}};
                            msj = new MessageSystemeJoueur(nvelleZone);
                            break;
                        case MODIF_INFOS:
                            System.out.println("un client a modifié ses infos" + mjs.getDescriptionJoueur());
                            msj = new MessageSystemeJoueur();
                            break;
                        default:
                            msj = new MessageSystemeJoueur();
                            break;
                    }
                    System.out.println(queue);
                    channelJoueur.basicPublish("",queue, null, Envoie.serialize(msj));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };

            // les queues "connexion" servent à rediriger les joueurs vers leurs propres moyens de communicaton
            String queueConnexion_Envoie = nom +"_Connexion_StoJ";
            String queueConnexion_Reception = nom + "_Connexion_JtoS";
            channelJoueur.queueDeclare(queueConnexion_Envoie, false, false, false, null);
            channelJoueur.queueDeclare(queueConnexion_Reception, false, false, false, null);

            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                try {
                    //synchronized (o) {
                        MessageJoueurSysteme mj = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                        if (mj.getType() == MessageJoueurSysteme.TypeMessage.MODIF_INFOS) {

                            // mise en place des nouvelles queues
                            String s = nom + cpt;

                            String nvelleQueue_Envoie = s + "_StoJ";
                            String nvelleQueue_Reception = s + "_JtoS";

                            channelJoueur.queueDeclare(nvelleQueue_Envoie, false, false, false, null);
                            channelJoueur.queueDeclare(nvelleQueue_Reception, false, false, false, null);
                            queueJoueur_Envoie.put(cpt, nvelleQueue_Envoie);
                            //queueJoueur_Reception.put(Integer.valueOf(cpt),nvelleQueue_Envoie);
                            channelJoueur.basicConsume(nvelleQueue_Reception, true, deliverCallbackJoueur, consumerTag2 -> {
                            });

                            cpt++;

                            // redirection du joueur vers ses propres queues (envoie & reception)
                            channelJoueur.basicPublish("", queueConnexion_Envoie, null, s.getBytes());
                        }
                    //}
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

            };
            channelJoueur.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });

            System.out.println("Zone " + nom + " active");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
