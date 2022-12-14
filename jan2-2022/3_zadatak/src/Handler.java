import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {


    @Override
    protected int getDefaultPort() {
        return ExchangeURLConnection.default_port;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new ExchangeURLConnection(u);
    }
}
