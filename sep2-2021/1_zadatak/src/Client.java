import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static final int port = 13370;
    public static final String hostname = "localhost";

    public static void main(String[] args) {

        try (Socket client = new Socket(hostname, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {


            String line;
            System.out.println("Current state of the farm:");
            line = in.readLine().replace('\t', '\n');
            System.out.println(line);

            while (true) {

                // saljemo serveru komandu
                System.out.println("Please insert the next command:");
                String command = scanner.nextLine();

                // pisemo serveru
                sendTo(out, command);

                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                // server nam odgovara
                line = in.readLine().replace('\t', '\n');
                System.out.println(line);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendTo(BufferedWriter out, String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

}
