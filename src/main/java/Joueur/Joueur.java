package Joueur;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import Message.MessageJoueurSysteme;
import Message.MessageSystemeJoueur;
import Outils.Envoie;
import UITextuel.AffichageTerrain;
import Types.*;

import java.io.IOException;

public class Joueur {
    private int id;
    private String emplacement;
    private Channel channel;
    private String queue_Envoie;
    private String queue_Reception;
    private static final Object o = new Object();

    /**
     * Créé un joueur et l'affecte à une zone aléatoirement
     * @param nom le nom du joueur
     * @param random le numéro de la zone à laquelle il sera affecté
     */
    public Joueur(String nom, int random) {
        this.id = Integer.parseInt(nom);
        switch (random){
            case 0:
                emplacement = "HG";
                break;
            case 1:
                emplacement = "HD";
                break;
            case 2:
                emplacement = "BG";
                break;
            default:
                emplacement = "BD";
                break;
        }

    }

    /**
     * Créé un joueur
     */
    public Joueur() {
        emplacement = "HG";
    }

    /**
     * Créé la connexion RabbitMQ côté Joueur
     */
    public void connexion() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection =  factory.newConnection();
            channel = connection.createChannel();

            // Permet le dialogue entre le systeme et le joueur
            DeliverCallback deliverCallbackSysteme = (consumerTag, delivery) -> {
                try {
                    // le message contient toujours une actualisation du terrain & quelque chose à afficher
                    MessageSystemeJoueur messageSystemeJoueur = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());
                    System.out.println(messageSystemeJoueur.getMessage());
                    System.out.println(AffichageTerrain.afficher(messageSystemeJoueur.getNvelleCarte()));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };

            // Utilisation des queues "connexion" pour créer ses propres communications (noms fixés)
            String queueConnexion_Envoie = emplacement + "_Connexion_JtoS";
            String queueConnexion_Reception = emplacement + "_Connexion_StoJ";

            channel.queueDeclare(queueConnexion_Envoie, false, false, false, null);
            channel.queueDeclare(queueConnexion_Reception, false, false, false, null);

            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                try {
                    MessageSystemeJoueur messageSystemeJoueur = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());

                    this.queue_Envoie = id + "_JtoS";
                    this.queue_Reception = id + "_StoJ";
                    channel.basicConsume(queue_Reception, true, deliverCallbackSysteme, consumerTag2 -> {
                    });

                    // Couper l'écoute sur la queue "connexion"
                    channel.basicCancel(consumerTag);
                    synchronized (o){
                        o.notify();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            };
            channel.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });

            // Il faut envoyer un premier message sur la queue connexion afin d'informer de notre présence
            MessageJoueurSysteme messageJoueurSysteme = new MessageJoueurSysteme(id, false);
            channel.basicPublish("", queueConnexion_Envoie, null, Envoie.serialize(messageJoueurSysteme));

            synchronized (o){
                try {
                    o.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Envoie un message lors du déplacement d'un joueur
     * @param deplacement le déplacement du joueur
     */
    public void deplacer(Deplacement deplacement) {
        MessageJoueurSysteme messageJoueurSysteme = new MessageJoueurSysteme(id, deplacement);
        envoieMessage(messageJoueurSysteme);
    }

    /**
     * Envoie un message lorsqu'un joueur veut quitter le jeu
     */
    public void quitter(){
        MessageJoueurSysteme messageJoueurSysteme = new MessageJoueurSysteme(id, true);
        envoieMessage(messageJoueurSysteme);
        System.exit(0);
    }

    /**
     * Envoie un message
     * @param message le message
     */
    private void envoieMessage(MessageJoueurSysteme message) {
        try {
            channel.basicPublish("", queue_Envoie, null, Envoie.serialize(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}