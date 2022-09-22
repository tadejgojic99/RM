import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Client {

    public static final int port = 12346;
    public static final int buff_size = 1025;

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("localhost", port);
        try (SocketChannel client = SocketChannel.open(address);
             Scanner scanner = new Scanner(System.in)) {

            client.configureBlocking(true);
            LinkedList<Integer> loto_combination = new LinkedList<Integer>();

            for (int i = 0; i < 7; ++i) {
                while (true) {
                    int val = scanner.nextInt();
                    if (!loto_combination.contains(val) || !(val >= 0 && val <= 39)) {
                        loto_combination.add(val);
                        break;
                    } else {
                        System.out.println("Vec si mi dao takvu cifru!");
                    }
                }
            }


            StringBuilder loto_combination_string = new StringBuilder();
            for (int el : loto_combination) {
                loto_combination_string.append(el).append(" ");
            }

            // ja prvo njemu pisem, pa onda on meni pise
            byte[] loto_combination_bytes = loto_combination_string.toString().getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(loto_combination_bytes.length + 1);

            buffer.put(loto_combination_bytes);

            buffer.flip();
            client.write(buffer);
            buffer.clear();

            ByteBuffer response = ByteBuffer.allocate(4);
            IntBuffer view = response.asIntBuffer();

            while (response.hasRemaining()) {
                client.read(response);
            }

            System.out.println("Odgovor sa servera: ");
            System.out.println(view.get());

            view.rewind();
            response.clear();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
