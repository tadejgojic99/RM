import java.io.*;
import java.nio.file.Path;

public class FileRunnable implements Runnable {

    public static String wanted_word;
    private Path file;

    public FileRunnable(Path entry) {
        this.file = entry;
    }

    @Override
    public void run() {

        try(BufferedReader in = new BufferedReader(new FileReader(this.file.toString()))) {

            StringBuilder result = new StringBuilder(this.file.getFileName().toString() + "\n");

            String line;
            int number_of_occurances = 0;
            String longest_line = "";
            int len_of_longes_line = 0;
            while ((line = in.readLine()) != null) {

                if (len_of_longes_line < line.trim().length()) {
                    longest_line = line.trim();
                    len_of_longes_line = line.trim().length();
                }

                for (String word : line.trim().split(" ")) {
                    if (word.trim().equalsIgnoreCase(wanted_word))
                        number_of_occurances++;
                }
            }

            result.append(longest_line).append("\n");
            result.append(number_of_occurances);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
