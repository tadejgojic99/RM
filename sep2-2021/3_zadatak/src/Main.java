import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Path dir = Paths.get("C:/Users/Vukan/Desktop/Cetvrta godina faks/1 semestar/Racunarske mreze/Rokovi/sep2-2021/3_zadatak/matrice");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry: stream) {
                if (entry.toString().endsWith(".txt")) {
                    // zapocinjemo nit
                    new Thread(new FileRunnable(entry)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
