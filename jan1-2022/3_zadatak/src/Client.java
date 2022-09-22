import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {

    public static final int buff_size = 1025;
    public static final int port = 12345;

    public static void main(String[] args) {

        try(DatagramSocket client = new DatagramSocket()) {

            // MOZDA
            //client.setSoTimeout(5000);

            InetAddress address = InetAddress.getByName("localhost");

            String file_name = "LastSupper.txt";
            DatagramPacket send_file_name = new DatagramPacket(file_name.getBytes(), file_name.length(), address, port);

            int file_line = 3;
            DatagramPacket send_file_line = new DatagramPacket(Integer.toString(file_line).getBytes(), Integer.toString(file_line).length(), address, port);

            client.send(send_file_name);
            client.send(send_file_line);

            byte[] buffer = new byte[buff_size];
            DatagramPacket response = new DatagramPacket(buffer, buff_size);

            client.receive(response);

            System.out.println(new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
