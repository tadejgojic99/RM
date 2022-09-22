import java.io.*;
import java.nio.file.Path;

public class FileRunnable implements Runnable {

    private Path file;
    public static String wanted_word;

    public FileRunnable(Path entry) {
        this.file = entry;
    }

    @Override
    public void run() {

        // citamo kroz fajl
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.file.toString())))) {

            StringBuilder result_of_thread = new StringBuilder();
            int line_number = 1;
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(wanted_word)) {

                    result_of_thread.append(this.file.toString() + ":" + line_number + ":"
                            + line.indexOf(wanted_word) + ": " + line + "\n");

                }
                line_number++;
            }

            System.out.print(result_of_thread.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
