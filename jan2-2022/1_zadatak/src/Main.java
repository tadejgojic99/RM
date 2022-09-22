import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Path dir = Path.of("1_zadatak/src/");



        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
             Scanner scanner = new Scanner(System.in);) {
            FileRunnable.wanted_word = scanner.next();
            for (Path entry: stream) {
                // svima je ista, sto da ne
                String entry_name = entry.toString();
                if (!entry_name.endsWith(".png") && !entry_name.endsWith(".ico") && !entry_name.endsWith(".svg"))
                    new Thread(new FileRunnable(entry)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
