import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static int port = 12345;
    public static String host = "localhost";

    public static void main(String[] args) {

        try(Socket client = new Socket(host, port);
            Scanner scanner = new Scanner(System.in);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            System.out.println("Molim vas ucitajte svoje ime:");
            String name;
            name = scanner.nextLine();
            // saljem mu ime klijenta!
            out.write(name);
            out.write("\n");
            out.flush();

            String command;
            while (true) {

                String job;
                command = scanner.nextLine();
                out.write(command);
                out.write("\n");
                out.flush();

                if (command.equalsIgnoreCase("izadji")) {
                    client.close();
                    break;
                }
                else if (command.equalsIgnoreCase("odradi")) {
                    job = in.readLine();
                    System.out.println("Vas zadatak je : " + job + ".");
                }
                else if (command.startsWith("dodaj") && !command.equalsIgnoreCase("dodaj")) {
                    job = command.split(" ")[1];
                    System.out.println("Posao koji si uneo da radis je: " + job);
                }
                else {
                    System.out.println("Ne prepoznajem tu komandu!");
                }



            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
