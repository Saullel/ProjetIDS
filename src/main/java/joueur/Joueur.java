package joueur;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import message.MessageJoueurSysteme;
import message.MessageSystemeJoueur;
import outils.Envoie;
import types.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Joueur {
    private int id;
    private DescriptionJoueur descriptionJoueur;
    private String emplacement;
    private Channel channel;
    private String queue_Envoie;
    private String queue_Reception;
    private static final Object o = new Object();

    /**
     * Créé un joueur
     * @param nom le nom du joueur
     */
    public Joueur(String nom) {
        this.id = Integer.parseInt(nom);
        emplacement = "HG";
    }

    /**
     * Créé un joueur
     */
    public Joueur() {
        emplacement = "HG";
    }

    /**
     * Créé la connexion RabbitMQ
     */
    public void connexion() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try {
            Connection connection =  factory.newConnection();
            channel = connection.createChannel();

            DeliverCallback deliverCallbackSysteme = (consumerTag, delivery) -> {
                try {
                    MessageSystemeJoueur messageSystemeJoueur = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());
                    switch (messageSystemeJoueur.getType()) {
                    	case MAJ_CARTE:
                            System.out.println("Voici le nouveau terrain" + messageSystemeJoueur.getNvelleCarte());
                            break;
                        case CHANGMT_ZONE:
                            //System.out.println("déplacement vers la zone" + mj.getDirectionDepl());
                            break;
                        case ERREUR:
                        	// TODO
                        	/*switch()
                        	  	case 1:
                        	  		System.out.println("Attention, il y a un mur");
                        	  	case 2:
                        	  		System.out.println("Attention, il y a un obstacle);
                        	  	case 3:
                        	  		System.out.println("Attention, il y a un autre joueur);
                        	 */
                        case INIT:
                        default:
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };

            // Utilisation des queues "connexion" pour créer ses propres communications
            String queueConnexion_Envoie = emplacement + "_Connexion_JtoS";
            String queueConnexion_Reception = emplacement + "_Connexion_StoJ";
            channel.queueDeclare(queueConnexion_Envoie, false, false, false, null);
            channel.queueDeclare(queueConnexion_Reception, false, false, false, null);

            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                try {
                    MessageSystemeJoueur messageSystemeJoueur = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());
                    if(messageSystemeJoueur.getType() == MessageSystemeToJoueur.MAJ_CARTE) {
                        String message = messageSystemeJoueur.getMessage();
                        this.queue_Envoie = id + "_JtoS";
                        this.queue_Reception = id + "_StoJ";
                        System.out.println(queue_Envoie + " " + queue_Reception);
                        channel.basicConsume(queue_Reception, true, deliverCallbackSysteme, consumerTag2 -> {
                        });

                        // Couper l'écoute sur la queue "connexion"
                        channel.basicCancel(consumerTag);
                        synchronized (o){
                            o.notify();
                        }
                    }
                    else {
                        System.out.println("Erreur de message. Demandé type INIT, reçu type " + messageSystemeJoueur.getType());
                        // TODO throw exception
                        System.exit(1);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            };
            channel.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });
            System.out.println(id);
            MessageJoueurSysteme messageJoueurSysteme = new MessageJoueurSysteme(id,false);
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

    public void deplacer(Deplacement d) {
        MessageJoueurSysteme messageJoueurSysteme = new MessageJoueurSysteme(id, d);
        envoieMessage(messageJoueurSysteme);
    }

    public void quitter(){
        MessageJoueurSysteme mjs = new MessageJoueurSysteme(id,true);
        envoieMessage(mjs);
    }

    private void envoieMessage(MessageJoueurSysteme m) {
        try {
            channel.basicPublish("", queue_Envoie, null, Envoie.serialize(m));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
