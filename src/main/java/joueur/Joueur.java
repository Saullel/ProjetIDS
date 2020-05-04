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
    private DescriptionJoueur dj;
    private String emplacement;
    private Channel channel;
    private String queue_Envoie;
    private String queue_Reception;
    private static final Object o = new Object();

    public Joueur(String nom, Forme forme, Couleur couleur){
        dj = new DescriptionJoueur(nom, types.Forme.CARRE, types.Couleur.ROUGE);
        emplacement = "HG";
    }

    public Joueur(){
        emplacement = "HG";
    }

    public void connexion()  {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection =  factory.newConnection();
            channel = connection.createChannel();

            DeliverCallback deliverCallbackSysteme = (consumerTag, delivery) -> {
                try {
                    MessageSystemeJoueur mj = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());
                    switch (mj.getType()) {
                    	case MAJ_CARTE:
                            System.out.println("voici le nouveau terrain" + mj.getNvelleCarte());

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

            // utilisation des queues "connexion" pour creer ses propres communications
            String queueConnexion_Envoie = emplacement + "_Connexion_JtoS";
            String queueConnexion_Reception = emplacement + "_Connexion_StoJ";
            channel.queueDeclare(queueConnexion_Envoie, false, false, false, null);
            channel.queueDeclare(queueConnexion_Reception, false, false, false, null);

            DeliverCallback deliverCallbackConnexion = (consumerTag, delivery) -> {
                try {
                    MessageSystemeJoueur mj = (MessageSystemeJoueur) Envoie.deserialize(delivery.getBody());
                    if(mj.getType() == MessageSystemeToJoueur.INIT) {
                        id = mj.getId();
                        String message = mj.getMessage();
                        this.queue_Envoie = message + "_JtoS";
                        this.queue_Reception = message + "_StoJ";
                        //channel.queueDeclare(queue_Envoie, false, false, false, null);
                        //channel.queueDeclare(queue_Reception, false, false, false, null);
                        channel.basicConsume(queue_Reception, true, deliverCallbackSysteme, consumerTag2 -> {
                        });

                        // couper l'écoute sur la queue "connexion"
                        channel.basicCancel(consumerTag);
                        synchronized (o){
                            o.notify();
                        }
                    }
                    else {
                        System.out.println("Erreur de message. Demandé type INIT, reçu type " + mj.getType());
                        //todo throw exception
                        System.exit(1);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            };
            channel.basicConsume(queueConnexion_Reception, true, deliverCallbackConnexion, consumerTag -> { });

            MessageJoueurSysteme mjs = new MessageJoueurSysteme(-1, -1);
            channel.basicPublish("", queueConnexion_Envoie, null, Envoie.serialize(mjs));

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
        MessageJoueurSysteme mjs = new MessageJoueurSysteme(id,d);
        envoieMessage(mjs);
    }

    /*public void quitter(){
        MessageJoueurSysteme mjs = new MessageJoueurSysteme(id);
        envoieMessage(mjs);
    }*/

   /* public void changerInfos(DescriptionJoueur dj) {
        MessageJoueurSysteme mjs = new MessageJoueurSysteme(id,);
        envoieMessage(mjs);
    }*/

    private void envoieMessage(MessageJoueurSysteme m) {
        try {
            channel.basicPublish("",queue_Envoie, null, Envoie.serialize(m));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
