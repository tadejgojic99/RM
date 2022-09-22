import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static final int port = 23456;
    public static final String host = "localhost";
    public static final int buff_size = 1025;

    public static void main(String[] args) {

        try (DatagramSocket client = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            client.setSoTimeout(5000);

            String request_line = scanner.nextLine();
            byte[] request_bytes = request_line.getBytes(StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(request_bytes, request_bytes.length,
                                                        InetAddress.getByName(host), port);
            client.send(request);


            byte[] response_bytes = new byte[buff_size];
            DatagramPacket response = new DatagramPacket(response_bytes, buff_size);
            client.receive(response);

            System.out.println(new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
