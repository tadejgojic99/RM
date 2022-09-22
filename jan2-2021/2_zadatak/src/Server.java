import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Server {

    public static final int port = 12345;
    public static final String host = "localhost";
    public static final int buff_size = 1025;

    public static void main(String[] args) {

        try (DatagramSocket server = new DatagramSocket(port)) {

            while (true) {

                // opsluzujemo nekog konkretnog klijenta
                try {

                    byte[] request_byte = new byte[buff_size];
                    DatagramPacket request = new DatagramPacket(request_byte, buff_size);
                    server.receive(request);
                    String request_string = new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8);

                    String response_string = alterString(request_string);
                    byte[] response_bytes = response_string.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket response = new DatagramPacket(response_bytes, response_bytes.length,
                                                                 request.getAddress(), request.getPort());
                    server.send(response);


                } catch (IOException e) {
                    // ne radi nista, samo jer je jedan klijent propao, ne mora da znaci da su svi!
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String alterString(String request_string) {
        StringBuilder sb = new StringBuilder();
        for (char c : request_string.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
                sb.append(Character.toLowerCase(c));
            }
            else if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            }
            else if (Character.isDigit(c)) {
                sb.append("..");
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
        // rad neki
        //return request_string;
    }

}
