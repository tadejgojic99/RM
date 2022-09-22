import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static final int port = 23456;
    public static final String host = "localhost";
    public static final int buff_size = 1025;

    private static Map<Character, String> morse_alphabet;


    public static void main(String[] args) {

        fillAlphabet();

        try (DatagramSocket server = new DatagramSocket(port)) {

            System.out.println("Starting server..");

            while (true) {

                try {
                    byte[] request_bytes = new byte[buff_size];
                    DatagramPacket request = new DatagramPacket(request_bytes, buff_size);
                    server.receive(request);

                    String request_string = new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8);

                    StringBuilder sb = new StringBuilder();
                    boolean no_problems = true;
                    for (char c : request_string.toCharArray()) {

                        if (!morse_alphabet.containsKey(Character.toLowerCase(c))) {
                            if (c == ' ') {
                                sb.append(" ");
                            } else {
                                sb = new StringBuilder("Ne smes da mi saljes nesto sto nije slovo.");
                                no_problems = false;
                                break;
                            }
                        } else {
                            sb.append(morse_alphabet.get(Character.toLowerCase(c))).append(" ");
                        }
                    }

                    if (no_problems) {
                        sb.append("*-*-*-");
                    }

                    System.out.println(sb.toString());

                    byte[] response_bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
                    DatagramPacket response = new DatagramPacket(response_bytes, response_bytes.length,
                            request.getAddress(), request.getPort());
                    server.send(response);
                } catch (IOException e) {
                    // nista, ako jedan klijent ne radi, necu da zajebem neke ostale
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void fillAlphabet() {

        morse_alphabet = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("2_zadatak/morse.txt")))) {
            String line;
            while ((line = in.readLine()) != null) {

                String[] line_parts = line.split("\t");
                morse_alphabet.put(Character.toLowerCase(line_parts[0].charAt(0)), line_parts[1]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
