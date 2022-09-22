import java.io.*;
import java.net.Socket;

public class ServeClientRunnable implements Runnable {

    private Socket client;
    private int n;

    public ServeClientRunnable(Socket client, int n) {
        this.client = client;
        this.n = n;
    }

    @Override
    public void run() {

        // obradi klijenta, i na kraju, da ga zatvori!
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            System.out.println("Accepted a client..");

            sendTo(out, Server.makeStringFromMatrixFarm());

            while (true) {

                // citamo sta nam je to poslao client
                String command = in.readLine().toLowerCase().trim();

                // radimo ono sto je rekao klijent
                if (command.startsWith("mark")) {
                    System.out.println("Marking..");
                    String[] parts_of_command = command.split(" ");
                    int i = Integer.parseInt(parts_of_command[1]);
                    int j = Integer.parseInt(parts_of_command[2]);

                    synchronized (Server.matrix_farm) {
                        Server.matrix_farm[i][j] = 'x';
                    }
                    sendTo(out, Server.makeStringFromMatrixFarm());
                }
                else if (command.startsWith("repair")) {
                    System.out.println("Repairing..");
                    String[] parts_of_command = command.split(" ");
                    int i = Integer.parseInt(parts_of_command[1]);
                    int j = Integer.parseInt(parts_of_command[2]);

                    synchronized (Server.matrix_farm) {
                        Server.matrix_farm[i][j] = 'o';
                    }
                    sendTo(out, Server.makeStringFromMatrixFarm());
                }
                else if (command.equalsIgnoreCase("list")) {
                    //saljemo trenutno stanje farme
                    System.out.println("Listing..");
                    sendTo(out, Server.makeStringFromMatrixFarm());
                }
                else if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting..");
                    client.close();
                    System.out.println("Closing a client..");
                    break;
                }
                else {
                    System.out.println("I dont know that command..");
                }

            }




        } catch (IOException e) {
            try {
                client.close();
                System.out.println("Something went wrong with a client, closing..");
            } catch (IOException ioException) {
                // nista ne radi
            }
        }

    }


    public void sendTo(BufferedWriter out, String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

}
