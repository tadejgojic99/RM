import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {

    public static final int port = 12346;
    public static final String hostname = "localhost";
    public static final int buf_size = 1025;
    private static LinkedList<Card> deck;

    public static void main(String[] args) {
        
        generateDeck();

        try (ServerSocketChannel server = ServerSocketChannel.open();
             Selector selector = Selector.open()){

            if (!selector.isOpen() || !server.isOpen()) {
                System.out.println("Oops, something went wrong, please try again.");
                System.exit(1);
            }

            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {

                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();
                    iter.remove();

                    try {

                        if (key.isAcceptable()) {
                            // server
                            System.out.println("Accepting client..");
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel client = serverSocketChannel.accept();

                            client.configureBlocking(false);
                            ByteBuffer buffer = ByteBuffer.allocate(buf_size);
                            SelectionKey client_key = client.register(selector, SelectionKey.OP_READ);
                            client_key.attach(buffer);
                            System.out.println("Accepted the client " + client.getRemoteAddress());

                        }
                        else if (key.isReadable()) {
                            // citamo sta nam klijent salje

                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            client.read(buffer);
                            buffer.flip();
                            int num_of_cards = buffer.getInt();
                            System.out.println("Client " + client.getRemoteAddress() + " wants " + num_of_cards);
                            buffer.clear();

                            // priprema onoga sto mi njemu saljemo
                            String response = getResponseToClient(num_of_cards);
                            byte[] response_byte = response.getBytes(StandardCharsets.UTF_8);
                            buffer.put(response_byte);
                            buffer.flip();

                            key.interestOps(SelectionKey.OP_WRITE);
                        }
                        else if (key.isWritable()) {
                            // pisemo mi klijentu
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            client.write(buffer);

                            if (!buffer.hasRemaining()) {
                                client.close();
                            }

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

    private static String getResponseToClient(int num_of_cards) {

        if (num_of_cards > deck.size()) {
            return "Don't have enough cards to give you! (" + deck.size() + ")";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num_of_cards; ++i) {
            Card card = deck.pop();
            sb.append(card).append("\n");
        }

        return sb.toString();

    }

    private static void generateDeck() {

        deck = new LinkedList<>();

        for (int sign = 0; sign < 4; sign++) {
            for(int card_number = 2; card_number < 15; card_number++) {
                deck.push(new Card(card_number, sign));
            }
        }

        System.out.println("Deck of cards:");
        Collections.shuffle(deck);
        for (Card card : deck) {
            System.out.println(card);
        }
        //System.out.println();
        System.out.println("=================================");

    }

}
