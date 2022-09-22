import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ExchangeURLConnection extends URLConnection {

    public static final int default_port = 12345;
    private Socket connection = null;

    public ExchangeURLConnection(URL u) {
        super(u);
    }

    @Override
    public String getContentType() {
        return "plain/text";
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        if (!connected) {
            connect();
        }
        return connection.getInputStream();
    }

    @Override
    public synchronized void connect() throws IOException {

        if (!connected) {

            int port = url.getPort();
            if (port <= 0 || port > 65535)
                port = default_port;

            this.connection = new Socket(url.getHost(), port);

            String query = url.getQuery();
            System.out.println(query);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
            out.write("EUR 5");
            out.flush();

            ByteBuffer buffer = ByteBuffer.allocate(8);
            DoubleBuffer view = buffer.asDoubleBuffer();

            SocketChannel client = connection.getChannel();

            //while (buffer.hasRemaining())
            client.write(buffer);

            System.out.println(view.get());

            System.out.println("Now we are connected!");
            this.connected = true;

        }


    }
}
