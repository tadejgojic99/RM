import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) {

        Path dir = Path.of("1_zadatak/tests/");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                new Thread(new CheckFileRunnable(entry)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
