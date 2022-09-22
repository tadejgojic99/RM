import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class FileRunnable implements Runnable {

    private Path file;

    public FileRunnable(Path entry) {
        this.file = entry;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.file.toString())))) {

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = in.readLine()) != null) {

                String[] line_parts = line.split(" ");
                try {
                    int n = Integer.parseInt(line_parts[0]);
                    URL url = new URL(line_parts[1]);
                    if (!isHostname(url)) {
                        sb.append(getMatrix(n));
                        sb.append(getVersion(url)).append(" ");
                    }
                    sb.append(url.getPath()).append(" ").append(url.getProtocol()).append("\n");

                } catch (MalformedURLException e) {
                    // do nothing
                }

            }

            System.out.print(file + "\n" + sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getVersion(URL url) {
        String hostname = url.getHost();
        if (hostname.contains(":")) {
            return "v6";
        }
        if (hostname.contains("."))
            return "v4";
        return "v4";
    }

    private String getMatrix(int n) {
        StringBuilder result = new StringBuilder();
        StringBuilder one_line = new StringBuilder("=".repeat(n));
        for (int i = 0; i < n; ++i) {
            one_line.replace(i, i + 1, ">");
            result.append(one_line).append("\n");
            one_line.replace(i, i + 1, "=");
        }
        //System.out.println(result);
        return result.toString();
    }

    private boolean isHostname(URL url) {

        String hostname = url.getHost();
        if (hostname.contains(":"))
            return false;

        if (hostname.split("\\.").length != 4)
            return true;

        for (char c : hostname.toCharArray()) {
            if (!Character.isDigit(c) && c != '.')
                return true;
        }

        return false;
    }


}
