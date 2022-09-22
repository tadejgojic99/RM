import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class QuizURLConnection extends URLConnection {

    private Socket connection = null;
    public static int defaultPort = 12321;

    public QuizURLConnection(URL u) {
        super(u);
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        if (!connected)
            connect();
        return this.connection.getInputStream();
    }

    @Override
    public synchronized void connect() throws IOException {

        if (!connected) {

            int port = url.getPort();
            if (0 > port || port > 65535) {
                port = defaultPort;
            }

            System.out.println("Making a socket");
            this.connection = new Socket(url.getHost(), port);

            // JAKO BITNO!
            // NE SMES DA RADIS TRY/CATCH => CLOSE CE TI SE STREAM!
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
            out.write(url.getQuery());
            out.newLine();
            out.flush();

            connected = true;
        }

    }


}
