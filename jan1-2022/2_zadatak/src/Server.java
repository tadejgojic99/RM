import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {

    public static final int port = 12345;
    public static final String host = "localhost";

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(port);
             PrintWriter out = new PrintWriter(new FileWriter("2_zadatak/log.txt"))) {

            System.out.println("Starting the server..");
            LinkedBlockingDeque<String> jobs = new LinkedBlockingDeque<String>();

            while (true) {

                Socket client = null;

                try {
                    client = server.accept();
                    new Thread(new ClientThread(client, out, jobs)).start();

                } catch (Exception e) {
                    System.out.println("Something went wrong with the current client.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
