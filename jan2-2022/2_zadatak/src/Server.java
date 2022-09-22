import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Server {

    public static final int port = 1337;
    private static Map<String, Double> kursevi;
    private static final int buff_size = 1025;

    public static void main(String[] args) {

        cacheFill();

        try (ServerSocketChannel server = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            if (!server.isOpen() || !server.isOpen()) {
                System.out.println("Something went wrong.");
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
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel client = serverSocketChannel.accept();
                            System.out.println("Client " + client.getRemoteAddress() + " accepted!");

                            client.configureBlocking(false);

                            ByteBuffer buffer = ByteBuffer.allocate(buff_size);

                            SelectionKey client_key = client.register(selector, SelectionKey.OP_READ);
                            client_key.attach(buffer);

                        }
                        else if (key.isReadable()) {
                            // klijent nam nesto pise

                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            // verovatno treba, jer necemo praviti novi buffer u sl instanti (PROBAJ, ZANIMA ME)
                            //buffer.clear();

                            client.read(buffer);
                            String res = new String(buffer.array(), 0, buffer.position());
                            System.out.println("Message from client " + client.getRemoteAddress() + " is : " + res);
                            if (res.equalsIgnoreCase("prekid")) {
                                System.out.println("Desio se prekid");
                                client.close();
                                continue;
                            }
                            buffer.clear();

                            // spremamo sta ce to da se salje klijentu.
                            double total_sum = get_total_sum(res);
                            System.out.println("Value we will send to our client: " + total_sum);
                            buffer.putDouble(total_sum);
                            //buffer.put((byte)'\n');
                            buffer.flip();
                            key.interestOps(SelectionKey.OP_WRITE);

                        }
                        else if (key.isWritable()) {
                            // nesto pisemo klijentu

                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            System.out.println("Writing to our client..");
                            client.write(buffer);

                            if (!buffer.hasRemaining()) {
                                System.out.println("We are finished writing to our client.. (for this iteration)");
                                key.interestOps(SelectionKey.OP_READ);
                                buffer.rewind();
                                System.out.println(buffer.getDouble());
                                buffer.clear();
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

    private static double get_total_sum(String res) {
        String[] res_parts = res.trim().split(" ");
        String kurs = res_parts[0];
        double value_needed = Double.parseDouble(res_parts[1]);
        if (!kursevi.containsKey(kurs))
            return -1;
        return kursevi.get(kurs) * value_needed;
    }

    private static void cacheFill() {
        kursevi = new HashMap<>();

        try (Scanner in = new Scanner(new InputStreamReader(new FileInputStream("2_zadatak/kursna_lista.txt")))) {

            while (in.hasNext()) {
                String kurs_name = in.next();
                Double kurs_value = in.nextDouble();
                kursevi.put(kurs_name, kurs_value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

