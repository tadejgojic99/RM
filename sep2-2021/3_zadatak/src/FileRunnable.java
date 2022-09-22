import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class FileRunnable implements Runnable {

    private Path file;

    public FileRunnable(Path entry) {
        file = entry;
    }

    @Override
    public void run() {

        try (Scanner in = new Scanner(new InputStreamReader(new FileInputStream(file.toString())))) {

            int n = in.nextInt();
            int m = in.nextInt();

            boolean matrix_is_under_triagular = true;

            int[][] matrix = new int[n][m];

            for (int i = 0; i < n; ++i)
                for(int j = 0; j < m; ++j)
                    matrix[i][j] = in.nextInt();

            for (int j = 0; j < m; ++j) {
                for (int i = j + 1; i < n; ++i) {
                    if (matrix[i][j] != 0) {
                        matrix_is_under_triagular = false;
                        break;
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(file.getFileName()).append(" ");
            if (matrix_is_under_triagular) {
                sb.append("da");
            }
            else {
                sb.append("ne");
            }


            System.out.println(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
