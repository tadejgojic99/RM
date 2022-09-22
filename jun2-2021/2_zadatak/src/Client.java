import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {


    public static final int port = 12321;
    public static final String hostname = "localhost";
    private static Map<String, List<Question>> quiz_questions;

    public static void main(String[] args) {

        try (Socket client = new Socket(hostname, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {
            
            // ime
            System.out.println("Molimo Vas unesite svoje ime: ");
            String name = scanner.nextLine();
            sendTo(out, name);

            // saljemo mu oblast koju ocemo
            System.out.println("Sve moguce teme: ");
            String possible_topics = in.readLine();
            System.out.println(possible_topics);

            // saljemo mu koju temu ocemo
            String topic = scanner.nextLine();
            sendTo(out, topic);

            System.out.println("Pocinje kviz...");

            while (true) {

                // prvo dobijam pitanje
                String question = in.readLine();
                System.out.println(question);
                // ako je pitanje  tipa "gotovo", onda gasi

                if (question.equalsIgnoreCase("Kviz je zavrsen.")) {
                    break;
                }
                // saljem mu odgovor
                String our_anwser = scanner.nextLine().trim();
                sendTo(out, our_anwser);

                // odgovor primamo (koliko smo poena dobili, i sve to)
                String response = in.readLine();
                System.out.println(response);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void sendTo(BufferedWriter out, String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }


}
