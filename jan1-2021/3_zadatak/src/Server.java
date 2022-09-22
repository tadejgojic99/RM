import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Server {

    public static final int port = 31415;
    public static final int buffer_size = 1025;

    public static void main(String[] args) throws SocketException {

        InetSocketAddress address = new InetSocketAddress(port);
        try(DatagramSocket socket = new DatagramSocket(address)) {

            while (true) {
                try {
                    byte[] buffer = new byte[buffer_size];
                    DatagramPacket request = new DatagramPacket(buffer, buffer_size);
                    socket.receive(request);

                    String received_data = new String(request.getData(), 0, request.getLength(), StandardCharsets.UTF_8);
                    double radius = Double.parseDouble(received_data);
                    System.out.println(radius);

                    double surface_area = radius * radius * Math.PI;
                    byte[] response_data = Double.toString(surface_area).getBytes(StandardCharsets.UTF_8);
                    DatagramPacket response = new DatagramPacket(response_data, response_data.length,
                            request.getAddress(), request.getPort());
                    socket.send(response);
                }
                catch (IOException e) {
                    // samo jedan klijent, ne moramo da se brinemo
                
            }

        }
        }


    }

}
