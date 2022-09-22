import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static final int port = 31415;
    public static final String host = "localhost";
    public static final int buffer_size = 1024;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(5000);

            String send_data = scanner.next();

            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket request = new DatagramPacket(send_data.getBytes(StandardCharsets.UTF_8),
                                                        send_data.length(), address, port);
            socket.send(request);

            byte[] response_data = new byte[buffer_size];
            DatagramPacket response = new DatagramPacket(response_data, response_data.length);
            socket.receive(response);

            System.out.println(new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8));



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
