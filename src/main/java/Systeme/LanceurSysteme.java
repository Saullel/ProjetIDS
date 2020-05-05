package Systeme;

import org.apache.cassandra.streaming.StreamOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LanceurSysteme {
    public static void main (String[] args) throws IOException {
        List<Process> serveurs = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        serveurs.add(Runtime.getRuntime().exec("java -jar target/LanceurServeur-jar-with-dependencies.jar --zone HG"));
        serveurs.add(Runtime.getRuntime().exec("java -jar target/LanceurServeur-jar-with-dependencies.jar --zone HD"));
        serveurs.add(Runtime.getRuntime().exec("java -jar target/LanceurServeur-jar-with-dependencies.jar --zone BG"));
        serveurs.add(Runtime.getRuntime().exec("java -jar target/LanceurServeur-jar-with-dependencies.jar --zone BD"));

        System.out.println("Le système est lancé");
        System.out.println("\"quit\" pour quitter");
        String cmd;
        do{
            cmd = scanner.nextLine();
        }while(!cmd.equals("quit"));

        for (Process process : serveurs) process.destroy();

    }
}
