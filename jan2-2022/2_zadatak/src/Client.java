import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    public static final int port = 12346;
    public static final String host = "localhost";
    public static final int buffer_size = 1024;

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress(host, port);
        try (SocketChannel client = SocketChannel.open(address);
             Scanner scanner = new Scanner(System.in)) {

            //client.configureBlocking(true);

            ByteBuffer buffer = ByteBuffer.allocate(buffer_size);
            DoubleBuffer view = buffer.asDoubleBuffer();

            String line;
            while (true) {

                line = scanner.nextLine();

                // prvo mi pisemo serveru:
                byte[] line_bytes = line.getBytes(StandardCharsets.UTF_8);
                buffer.put(line_bytes);
                buffer.flip();
                // poslali smo bufferu sve sto imamo
                client.write(buffer);
                buffer.clear();

                // sada nama server pise:
                client.read(buffer);

                view.rewind();
                System.out.println("Client has read everything..");
                System.out.println(view.get());
                buffer.clear();

                if (line.trim().equalsIgnoreCase("prekid"))
                    break;

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
