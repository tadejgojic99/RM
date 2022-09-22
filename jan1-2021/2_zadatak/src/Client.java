import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static final int port = 12345;
    public static final String hostname = "localhost";

    public static void main(String[] args) {

        try (Socket client = new Socket(hostname, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            while (true) {

                // ako mi je server rekao da je gotova partija, izlazim jelte
                String server_return = in.readLine();

                if (server_return.trim().equalsIgnoreCase("finished")) {
                    break;
                }
                // 1.
                // u suprotnom, poslao mi je novo stanje igre, koje cu ispisem
                drawTable(server_return);
                // 2. => get player turn
                String my_turn = scanner.next();
                // 3. => send player turn
                out.write(my_turn);
                out.newLine();
                out.flush();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void drawTable(String state_of_game) {
        System.out.println("\n" + state_of_game + "\n");
        // mozemo da saljemo kao --X\n-X-\O-O\n => MOZDA UMESTO \N, SAMO SPACE
        if (state_of_game.contains("--")) {
            System.out.println(state_of_game.substring(0, 3));
            System.out.println(state_of_game.substring(3, 6));
            System.out.println(state_of_game.substring(6, 9));
        }
        else
            System.out.println(state_of_game);

    }


}
