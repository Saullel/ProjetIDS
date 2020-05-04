package graphique;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import joueur.Joueur;
import types.Deplacement;

public class Main extends Application {

	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("THIS IS NOT A GAME");
		Canvas canvas = new Canvas(600, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		VBox vbox = new VBox(canvas);
		Scene scene = new Scene(vbox);
		gc.setStroke(Color.rgb(0, 107, 159));
		gc.setLineWidth(3);
		gc.setFill(Color.rgb(230, 230, 255));
		gc.fillRect(0, 0, 600, 600);
		
		int size = 150;
		
		for (int i = 0; i <= 600/size; i++) {
			gc.strokeLine(i*size, 0, i*size, 600);
			gc.strokeLine(0, i*size, 600, i*size);
		}
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Joueur j = new Joueur();
        //j.connexion();
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	System.out.println("TEST");
                switch (event.getCode()) {
                    case UP:
                    	//j.deplacer(Deplacement.HAUT);
                    	System.out.println("en haut");
                    	break;
                    case DOWN:
                    	//j.deplacer(Deplacement.BAS);
                    	System.out.println("en bas");
                    	break;
                    case LEFT:
                    	//j.deplacer(Deplacement.GAUCHE);
                    	System.out.println("à gauche");
                    	break;
                    case RIGHT:
                    	//j.deplacer(Deplacement.DROIT);
                    	System.out.println("à droite");
                    	break;
                    case ESCAPE:
                    	// j.quitter();
                    	System.out.println("on se nachave");
                    default:
                    	break;
                }
            }
        });
        
        
	}
}