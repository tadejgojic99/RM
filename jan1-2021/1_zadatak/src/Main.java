import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Path dir = Paths.get("1_zadatak/src");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
             Scanner scanner = new Scanner(System.in)) {
            FileRunnable.wanted_word = scanner.next();
            for (Path entry: stream) {
                new Thread(new FileRunnable(entry)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
