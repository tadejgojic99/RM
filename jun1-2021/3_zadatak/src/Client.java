import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    public static final int port = 12346;
    public static final String hostname = "localhost";
    public static final int buf_size = 1025;

    public static void main(String[] args) {

        InetSocketAddress address =  new InetSocketAddress(hostname, port);
        try (SocketChannel client = SocketChannel.open(address);
             Scanner scanner = new Scanner(System.in)) {

            client.configureBlocking(true);

            int num_of_cards = scanner.nextInt();
            System.out.println(num_of_cards);

            ByteBuffer buffer = ByteBuffer.allocate(buf_size);
            buffer.putInt(num_of_cards);
            buffer.flip();
            client.write(buffer);

            buffer.clear();
            int n;

            while (true) {

                n = client.read(buffer);
                if (n > 0) {
                    // nesto je procitao
                    String res = new String(buffer.array(), 0, buffer.position());
                    System.out.println(res);
                    buffer.clear();
                }
                if (n == -1) {
                    // server nam je poslao sve sto ima
                    break;
                }


            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
