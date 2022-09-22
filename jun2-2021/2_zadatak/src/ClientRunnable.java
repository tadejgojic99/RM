import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ClientRunnable implements Runnable {

    private Socket client;
    private String name;
    private String topic;
    private int number_of_points;

    public ClientRunnable(Socket client) throws IOException {
        this.client = client;
    }

    @Override
    public void run() {


        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            // read the name the user sent us.
            this.name = in.readLine();
            System.out.println("Dobrodosao " + this.name + " u kviz");

            StringBuilder sb = new StringBuilder();
            for (var topic : Server.quiz_questions.entrySet()) {
                sb.append(topic.getKey()).append(" ");
            }

            sendTo(out, sb.toString());

            System.out.println("Saljem " + this.name + " sve moguce teme");

            // cekamo da nam posalje koju temu zeli
            this.topic = in.readLine().trim();
            System.out.println("Igrac " + this.name + " je izabrao temu " + this.topic);

            System.out.println("Kviz pocinje, pripremite se..");

            for (Question question : Server.quiz_questions.get(this.topic)) {
                client.setSoTimeout(5000);
                // saljemo mu pitanje
                sendTo(out, question.getQuestion_text());

                // gledamo odgovor
                try {
                    String client_anwser = in.readLine();
                    if (client_anwser.trim().equalsIgnoreCase(question.getQuestion_anwser())) {
                        sendTo(out, "Tačan odgovor. Dobili ste " + question.getQuestion_points() + " poen.");
                        this.number_of_points += question.getQuestion_points();
                    } else if (client_anwser.trim().equalsIgnoreCase("Ne znam")) {
                        sendTo(out, "Niste znali tačan odgovor.");
                    } else {
                        sendTo(out, "Netačan odgovor. Izgubili ste " + question.getQuestion_points() + " poen.");
                        this.number_of_points -= question.getQuestion_points();
                    }
                }
                catch (SocketTimeoutException e) {
                    System.out.println("Proslo je vreme");
                    client.setSoTimeout(0);
                    String client_anwser = in.readLine();
                    sendTo(out, "Niste stigli da odgovorite na vreme.");
                }
            }

            System.out.println("Zavrsen je kviz za igraca " + this.name + " koji je ostvario : " + this.number_of_points);
            sendTo(out,"Kviz je zavrsen.");
            client.close();
            // TODO: ako je medju najbolja 3, jos jedna poruka

        } catch (IOException e) {
            // nista ne radis, jer ako se nes desi klijentu, ocemo samo dalje nastavimo.
            System.out.println("Something went wrong with " + this.name +   ", closing..");
            try {
                client.close();
            } catch (IOException ioException) {
                // nista
            }
        }




    }


    private static void sendTo(BufferedWriter out, String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

}
