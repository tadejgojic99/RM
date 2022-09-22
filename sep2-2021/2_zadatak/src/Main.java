import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        URL url = new URL(null, "farm://localhost:13370?q=mark&x=3&y=4", new Handler());
        var connection = url.openConnection();

        String line;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.   getInputStream()))) {
            while ((line = in.readLine()) != null) {
                System.out.println(line.replace("\t", "\n"));
            }
        }

    }

}
