import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Server {

    public static final int port = 12346;
    public static final int buff_size = 1025;

    public static void main(String[] args) {

        try (ServerSocketChannel server = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            if (!selector.isOpen() || !server.isOpen()) {
                System.out.println("Neka greska");
                System.exit(1);
            }

            InetSocketAddress address = new InetSocketAddress(port);
            server.bind(address);
            server.configureBlocking(false);

            server.register(selector, SelectionKey.OP_ACCEPT);

			LinkedList<Integer> winner_combination = generate_random_combination();
            System.out.println(winner_combination);

            while (true) {

                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                while (iter.hasNext()) {

                    SelectionKey key = iter.next();
                    iter.remove();

                    try {

                        if (key.isAcceptable()) {
                            // server

                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel client = serverSocketChannel.accept();
                            System.out.println("Sever accepted the client!");

                            ByteBuffer buffer = ByteBuffer.allocate(buff_size);

                            client.configureBlocking(false);
                            SelectionKey client_key = client.register(selector, SelectionKey.OP_READ);
                            client_key.attach(buffer);
                            System.out.println("Server attached a buffer!");

                        } else if (key.isReadable()) {
                            // citamo sta nam je klijent poslao
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            client.read(buffer);
                            String client_combination = new String(buffer.array(), 0, buffer.position());
                            buffer.clear();
                            System.out.println("Kombinacija koju nam je poslao klijent " + client.getRemoteAddress());
                            System.out.println(client_combination);

                            ByteBuffer new_buffer = ByteBuffer.allocate(4);
                            // kasnije cemo da izmenimo sta se salje zapravo
                            int number_of_hits = count_number_of_hits(winner_combination, client_combination);
                            new_buffer.putInt(number_of_hits);
                            new_buffer.flip();
                            key.attach(new_buffer);	

                            key.interestOps(SelectionKey.OP_WRITE);

                        } else if (key.isWritable()) {
                            // pisemo klijentu ono sto se vec nalazi u baffer-u
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer client_buffer = (ByteBuffer) key.attachment();

                            System.out.println("Sad mi njemu saljemo!");
                            client.write(client_buffer);

                            if (!client_buffer.hasRemaining()) {
                                client.close();
                            }

                        } else {
                            System.out.println("Neka greska!");
                            System.exit(1);
                        }
                    } catch (IOException e) {
                        key.cancel();
                        key.channel().close();
                    }

                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int count_number_of_hits(LinkedList<Integer> winner_combination, String client_combination) {
        int counter = 0;
        for (String s : client_combination.split(" ")) {
            if (winner_combination.contains(Integer.parseInt(s))) {
                counter++;
            }
        }
        return counter;
    }

    private static LinkedList<Integer> generate_random_combination() {
        LinkedList<Integer> combination = new LinkedList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 7; ++i) {
            while (true) {
                int el = random.nextInt(39) + 1;
                if (!combination.contains(el)) {
                    combination.push(el);
                    break;
                }
            }
        }
        return combination;
    }

}
