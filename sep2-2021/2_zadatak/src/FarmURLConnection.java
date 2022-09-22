import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class FarmURLConnection extends URLConnection {

    public static final int default_port = 13370;
    private Socket connection = null;


    public FarmURLConnection(URL u) {
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
        return connection.getInputStream();
    }

    @Override
    public synchronized void connect() throws IOException {

        if (!connected) {

            int port = url.getPort();
            if (port < 0 || port > 65355) {
                port = default_port;
            }

            connection = new Socket(url.getHost(), port);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            String query = url.getQuery();
            query = query.replaceAll("&+", " ");
            String[] query_parts = query.split(" ");
            StringBuilder command = new StringBuilder();
            command.append(query_parts[0].substring(query_parts[0].indexOf('=') + 1)).append(" ")
                   .append(query_parts[1].substring(query_parts[1].indexOf('=') + 1)).append(" ")
                   .append(query_parts[2].substring(query_parts[2].indexOf('=') + 1));


            System.out.println(command.toString());
            out.write(command.toString());
            out.newLine();
            out.flush();


            out.write("exit");
            out.newLine();
            out.flush();

            connected = true;
        }

    }

}
