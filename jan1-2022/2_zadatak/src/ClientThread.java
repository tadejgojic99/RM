import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientThread implements Runnable {

    private Socket client;
    private final PrintWriter log;
    private BufferedReader in;
    private BufferedWriter out;

    private String name;
    private LinkedBlockingDeque<String> jobs;

    public ClientThread(Socket client, PrintWriter log, LinkedBlockingDeque<String> jobs) throws IOException {
        this.client = client;
        this.log = log;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.jobs = jobs;
    }

    @Override
    public void run() {

        try {

            this.name = in.readLine();
            System.out.println("New user added: " + this.name);

            while (true) {

                String command;
                String log_text = "";
                if ((command = this.in.readLine()) == null) {
                    System.out.println("Izlazi " + this.name);
                    this.client.close();
                    break;
                }

                if (command.equalsIgnoreCase("izadji")) {
                    System.out.println("Izlazi " + this.name);
                    this.client.close();
                    break;
                }
                else if (command.startsWith("dodaj") && !command.equalsIgnoreCase("dodaj")) {

                    String job = command.split(" ")[1];
                    this.jobs.add(job);
                    System.out.println("Currently, the job queue looks like this:" );
                    System.out.println(this.jobs);
                    log_text = new Date() + ". Korisnik " + this.name + " je dodao zadatak " + job + "\n";
                }
                else if (command.equalsIgnoreCase("odradi")) {

                    if (this.jobs.isEmpty()) {
                        System.out.println("Nema posla koji mozes da uradis, sacekaj malo.");
                        this.out.write("-\n");
                        this.out.flush();
                    }
                    else {
                        String job = this.jobs.remove();
                        System.out.println("Posao koji ce " + this.name + " da radi je: " + job);
                        this.out.write(job + "\n");
                        this.out.flush();
                        log_text = new Date() + ". Korisnik " + this.name + " je odradio zadatak " + job + "\n";
                    }

                }

                synchronized (this.log) {
                    this.log.write(log_text);
                    this.log.flush();
                }


            }


        } catch (IOException e) {
            System.out.println("Nesto se lose desilo sa user-om " + this.name);
        }
    }
}
