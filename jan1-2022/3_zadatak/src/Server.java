import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Server {

    public static final int port = 12345;
    public static final int buff_size = 1025;

    public Map<String, List<String>> cache;


    public Server() {
        this.cache = new HashMap<>();
        this.initChache();
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.startWorking();
    }

    private void startWorking() {

        try (DatagramSocket server = new DatagramSocket(port)) {

            server.setSoTimeout(5000);

            while(true) {

                try {
                    // ako prodje vreme, gasi se veza
                    byte[] buffer = new byte[buff_size];

                    DatagramPacket request1 = new DatagramPacket(buffer, buff_size);
                    server.receive(request1);
                    System.out.println("Received req1");
                    String res1 = new String(request1.getData(), 0, request1.getLength(), StandardCharsets.UTF_8);
                    res1 = res1.trim();

                    DatagramPacket request2 = new DatagramPacket(buffer, buff_size);
                    server.receive(request2);
                    System.out.println("Received req2");
                    String res2 = new String(request2.getData(), 0, request2.getLength(), StandardCharsets.UTF_8);
                    res2 = res2.trim();


                    // prvo mi je stigo string, pa broj
                    String return_val;
                    if (res1.contains(".")) {
                        int num_line = Integer.parseInt(res2);

                        if (this.cache.containsKey(res1) && this.cache.get(res1).size() >= num_line)
                            return_val = this.cache.get(res1).get(num_line - 1);
                        else
                            return_val = "That file, or that line do not exist!";

                    } else {
                        int num_line = Integer.parseInt(res1);

                        if (this.cache.containsKey(res2) && this.cache.get(res2).size() >= num_line)
                            return_val = this.cache.get(res2).get(num_line - 1);
                        else
                            return_val = "That file, or that line do not exist!";

                    }

                    System.out.println("Sending package..");
                    DatagramPacket response = new DatagramPacket(return_val.getBytes(), return_val.length(),
                                                                request2.getAddress(), request2.getPort());

                    server.send(response);

                    System.out.println("Client " + request2.getAddress() + " has been served.");



                } catch (IOException e) {
                    System.out.println("Error");
                    // do nothing, necemo zbog jednog prodadnemo (or will we=)
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void initChache() {

        Path dir = Paths.get("3_zadatak/tests");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                try (BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(entry.toString())))) {
                    String line;
                    LinkedList<String> all_lines = new LinkedList<String>();
                    while ((line = fr.readLine()) != null) {
                        StringBuilder sb = new StringBuilder(line);
                        all_lines.add(sb.reverse().toString());
                    }
                    this.cache.put(entry.getFileName().toString(), all_lines);
                }
            }
            //System.out.println(this.cache);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
