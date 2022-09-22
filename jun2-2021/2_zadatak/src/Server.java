import com.sun.source.tree.SynchronizedTree;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Server {


    public static final int port = 12321;
    public static final String hostname = "localhost";
    public static Map<String, LinkedList<Question>> quiz_questions;
    public static Map<String, Queue<Integer>> best_results;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(port);
             Scanner scanner = new Scanner(System.in)) {

            // ovo se ucitava sa stand ulaza, ali me to mrzi svaki put radim
            String dir_path = "2_zadatak/kviz";
            fillQuizQuestions(dir_path);
            best_results = Collections.synchronizedMap(new HashMap<String, Queue<Integer>>());


            // napravicemo sync niz, tako da mozemo da odrzavamo top 3 bez vecih problema
            while (true) {
                try {

                    Socket client = server.accept();
                    System.out.println("Accepting a client..");
                    // nit koja obradjuje jednog client-a
                    new Thread(new ClientRunnable(client)).start();

                } catch (IOException e) {
                    // do nothing, ne zelimo ako jedan klijent propadne, da sve propadne
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void fillQuizQuestions(String dir_path) {

        quiz_questions = new HashMap<>();
        Path dir = Paths.get(dir_path);
        LinkedList<Question> questions;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry: stream) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(entry.toString())))) {
                    questions = new LinkedList<Question>();
                    String line;
                    while ((line = in.readLine()) != null) {
                        String question = line.substring(0, line.indexOf('?') + 1);
                        String anwser = line.substring(line.indexOf('?') + 1, line.lastIndexOf(' '));
                        String num_of_points = line.substring(line.lastIndexOf(' ') + 1);

                        questions.push(new Question(question.trim(), anwser.trim(), Integer.parseInt(num_of_points.trim())));
                    }
                    quiz_questions.put(entry.toString().substring(entry.toString().lastIndexOf('\\') + 1, entry.toString().lastIndexOf('.')), questions);
                }
            }

//            for (var el : quiz_questions.entrySet()) {
//                System.out.println(el);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
