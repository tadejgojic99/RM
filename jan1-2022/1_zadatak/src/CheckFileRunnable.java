import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class CheckFileRunnable implements Runnable {

    private Path path;

    public CheckFileRunnable(Path entry) {
        this.path = entry;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.path.toString())))) {

            StringBuilder entire_output = new StringBuilder();
            entire_output.append(this.path + "\n");
            String line;
            int line_number = 1;
            int number_of_valid_urls = 0;
            while((line = in.readLine()) != null) {
                line_number++;
                try {
                    URL url = new URL(line);
                    number_of_valid_urls++;
                    entire_output.append(line_number + " : " + url.getProtocol());
                    if (url.getProtocol().equals("https")) {
                        entire_output.append(" : " + url.getPort());
                    }
                    entire_output.append("\n");
                } catch (MalformedURLException e) {
                    // nista ne radimo ako nije okej!
                }

            }

            entire_output.append(number_of_valid_urls);

            System.out.println(entire_output);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
