import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public class FileRunnable implements Runnable {

    private Path file;

    public FileRunnable(Path entry) {
        this.file = entry;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString())))) {

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    URL url = new URL(line);
                    boolean is_hostname = isHostname(url);
                    if (!is_hostname) {
                        sb.append("(").append(getVersion(url)).append(") ");
                    }
                    sb.append(url.getProtocol()).append(" ")
                      .append(url.getAuthority()).append(" ")
                      .append(url.getPath()).append(" ");
                    if (!is_hostname && getVersion(url).equalsIgnoreCase("v4")) {
                        sb.append("[").append(url.getHost().replace(".", " ")).append("]");
                    }
                    sb.append("\n");

                } catch (MalformedURLException e) {
                    //System.out.println(line);
                    //System.out.println(file);
                    //System.out.println("------------------------------");
                    // nista ne radimo, ako je los
                }

            }
            if (sb.toString().isEmpty()) {
                System.out.println(file.toString() + " : Prazan");
                System.out.println("------------------------------------------------");
            }
            else {
                System.out.print(file.toString() + "\n" + sb.toString());
                System.out.println("------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getVersion(URL url) {
        String host = url.getHost();
        if (host.contains(":"))
            return "v6";
        if (host.contains("."))
            return "v4";
        return "v4";
    }

    private boolean isHostname(URL url) {

        String host = url.getHost();
        if (host.contains(":"))
            return false;
        if (host.split("\\.").length != 4)
            return true;

        for (char c : host.toCharArray()) {
            if ((!Character.isDigit(c) && c != '.')) {
                return true;
            }
        }

        return false;


    }
}
