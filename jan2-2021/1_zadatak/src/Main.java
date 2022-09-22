import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//        String file_path = scanner.next();
//        scanner.close();
        // mislim da treba da se ucita, al mrzi me, samo sam odma uneo lol


        // mozes ovde Scanner od BUfferedReader
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("1_zadatak/1.txt")))) {

            String line;
            while ((line = in.readLine()) != null) {

                try {
                    line = line.replace("[", " ");
                    String[] parts_of_line = line.split("]");
                    String time = parts_of_line[0].trim();
                    String ip_address = parts_of_line[1].trim();
                    String path = parts_of_line[2].trim();

                    URL url = new URL(path);

                    String protocol = url.getProtocol();
                    int default_port = url.getDefaultPort();
                    int port = url.getPort();
//                    System.out.println(default_port);
//                    System.out.println(port);

                    if ((protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https"))
                            && checkIFBeforeCurrentTime(time) && default_port == port) {
                        StringBuilder sb = new StringBuilder("");
                        String version = checkVersion(ip_address);
                        sb.append(version).append(":");
                        sb.append(protocol).append(":");
                        sb.append(url.getPath());
                        System.out.println(sb.toString());
                    }


                }catch (MalformedURLException e) {
                    // nista ne radimo (npr, za FILE nece znati protokol, ne zelimo da nas ugasi)
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static boolean checkIFBeforeCurrentTime(String url_time) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = formater.parse(url_time);
        return date.before(new Date());
    }

    // pitaj kacu da li moze ovako da se proveri
    private static String checkVersion(String ip_address) {
        if (ip_address.contains(".")) {
            return "v4";
        }
        if (ip_address.contains(":")) {
            return "v6";
        }
        return "unknown";
    }

}
