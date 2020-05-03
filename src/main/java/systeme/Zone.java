package systeme;

import types.MessageJoueurToSysteme;
import com.rabbitmq.client.*;
import message.*;
import outils.Envoie;
import types.Deplacement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Zone {
    String nom;
    int[][] partieTerrain;
    List<Channel> channelSysteme;
    Channel channelJoueur;

    public Zone() throws Exception {
        nom = "unique";
        partieTerrain = new int[][]{{0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0}};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection =  factory.newConnection();
            channelJoueur = connection.createChannel();
            channelJoueur.exchangeDeclare(nom,"fanout");
            String queueConnexion = channelJoueur.queueDeclare().getQueue();
            channelJoueur.queueBind(queueConnexion, nom, "");

            DeliverCallback deliverCallbackJoueur = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                try {
                    MessageJoueurSysteme mj = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                    switch (mj.getType()) {
                        case QUITTE:
                            System.out.println("un joueur souhaite partir");
                            break;
                        case DEPLACEMENT:
                            System.out.println("déplacement vers " + mj.getDirectionDepl());
                            break;
                        case MODIF_INFOS:
                            System.out.println("un client a modifié ses infos" + mj.getDescriptionJoueur());
                            break;
                        default:
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };


            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                try {
                    MessageJoueurSysteme mj = (MessageJoueurSysteme) Envoie.deserialize(delivery.getBody());
                    if(mj.getType().equals(MessageJoueurToSysteme.MODIF_INFOS)) {
                        String s = "nomQueue";
                        channelJoueur.basicPublish("", queueConnexion, null, s.getBytes());

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            channelJoueur.basicConsume(queueConnexion, true, deliverCallbackConnexion, consumerTag -> { });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
