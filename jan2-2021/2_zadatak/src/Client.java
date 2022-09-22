import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static final int port = 12345;
    public static final String host = "localhost";
    public static final int buff_size = 1025;

    public static void main(String[] args) {

        try (DatagramSocket client = new DatagramSocket();
             Scanner in = new Scanner(System.in)) {

            client.setSoTimeout(5000);

            String request_value = in.nextLine();
            InetAddress address = InetAddress.getByName(host);
            byte[] request_value_bytes = request_value.getBytes(StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(request_value_bytes, request_value.length(), address, port);
            client.send(request);

            byte[] response_bytes = new byte[buff_size];
            DatagramPacket response = new DatagramPacket(response_bytes, buff_size);
            client.receive(response);

            String response_string = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
            System.out.println(response_string);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
