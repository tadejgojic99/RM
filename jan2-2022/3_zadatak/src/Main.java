import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        URL url = new URL(null, "exchange://localhost:1337?valuta=EUR&iznos=5", new Handler());
        URLConnection connection = url.openConnection();

        connection.connect();

    }

}
