import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static final int port = 13370;
    public static final String hostname = "localhost";
    public static char[][] matrix_farm;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(port);
             Scanner scanner = new Scanner(System.in)) {

            int n = scanner.nextInt();
            makeMatrixFarm(n);
            System.out.println(makeStringFromMatrixFarm().trim().replace('\t', '\n'));

            while (true) {

                try {

                    Socket client = server.accept();
                    // pokrecemo nit da ga obradi, i da ga ugasi
                    new Thread(new ServeClientRunnable(client, n)).start();

                } catch (IOException e) {
                    // nista, jer ako ej jedan klijent crko, ne mora svaki!
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void makeMatrixFarm(int n) {

        matrix_farm = new char[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                matrix_farm[i][j] = 'o';
            }
        }

    }

    public static String makeStringFromMatrixFarm() {
        // pravimo string, koji ce biti zapravo onaj izlaz sto vidimo tamo
        int n = matrix_farm.length;

        StringBuilder sb = new StringBuilder();
        sb.append("+|");
        for (int i = 0; i < n; ++i) {
            sb.append(i);
        }
        sb.append("\t");
        sb.append("++");
        for (int i = 0; i < n; ++i) {
            sb.append("-");
        }
        sb.append("\t");

        for (int i = 0; i < n; ++i) {
            sb.append(i).append("|");
            for (int j = 0; j < n; ++j) {
                sb.append(matrix_farm[i][j]);
            }
            sb.append("\t");
        }

        return sb.toString().trim();
    }


}
