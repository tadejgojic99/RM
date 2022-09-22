import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int port = 12345;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(port)) {


            // kad se zavrsi jedna partija, moze da pocne sledeca!
            while (true) {

                StringBuilder state_of_game = new StringBuilder("---------");
                try (Socket player1 = server.accept();
                     BufferedReader player1_in = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                     BufferedWriter player1_out = new BufferedWriter(new OutputStreamWriter(player1.getOutputStream()));
                     Socket player2 = server.accept();
                     BufferedReader player2_in = new BufferedReader(new InputStreamReader(player2.getInputStream()));
                     BufferedWriter player2_out = new BufferedWriter(new OutputStreamWriter(player2.getOutputStream()))) {

                    System.out.println("Starting the game..");
                    // igranje jedne partije (player1 i player2 su prihvaceni, sada igrau do nekog trenutka)
                    while (true) {

                        // poslali smo prvom igracu kako izgleda
                        boolean game_finished = checkIfGameFinished(state_of_game);
                        if (game_finished) {
                            write_to_user(player1_out, "finished");
                            write_to_user(player2_out, "finished");
                            // gotov game, mzoe se zatvore socketi
                            break;
                        }
                        else {
                            write_to_user(player1_out, state_of_game.toString());
                        }

                        // cekamo da nam posalje gde hoce on da stavi vrednost (ako je tu slobodno!)
                        while (true) {
                            int player1_move = Integer.parseInt(player1_in.readLine().trim()) - 1;
                            if (state_of_game.length() > player1_move && state_of_game.charAt(player1_move) == '-') {
                                state_of_game.setCharAt(player1_move, 'X');
                                //write_to_user(player1_out, state_of_game.toString());
                                break;
                            }
                            else {
                                write_to_user(player1_out, "Unable to play there, please try again");
                            }
                        }

                        // isto, samo player2!


                        // poslali smo prvom igracu kako izgleda
                        game_finished = checkIfGameFinished(state_of_game);
                        if (game_finished) {
                            write_to_user(player1_out, "finished");
                            write_to_user(player2_out, "finished");
                            // gotov game, mzoe se zatvore socketi
                            break;
                        }
                        else {
                            write_to_user(player2_out, state_of_game.toString());
                        }

                        // cekamo da nam posalje gde hoce on da stavi vrednost (ako je tu slobodno!)
                        while (true) {
                            int player2_move = Integer.parseInt(player2_in.readLine().trim()) - 1;
                            if (state_of_game.length() > player2_move && state_of_game.charAt(player2_move) == '-') {
                                state_of_game.setCharAt(player2_move, 'O');
                                //write_to_user(player2_out, state_of_game.toString());
                                break;
                            }
                            else {
                                write_to_user(player2_out, "Unable to play there, please try again");
                            }
                        }



                    }


                } catch (IOException e) {
                    // ako jedan klijent propadne, sta onda?
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void write_to_user(BufferedWriter out, String msg) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }

    private static boolean checkIfGameFinished(StringBuilder state_of_game) {
        if ((state_of_game.charAt(0) != '-' && state_of_game.charAt(0) == state_of_game.charAt(1) && state_of_game.charAt(2) == state_of_game.charAt(1)) ||
                 (state_of_game.charAt(3) != '-' && state_of_game.charAt(3) == state_of_game.charAt(4) && state_of_game.charAt(4) == state_of_game.charAt(5)) ||
        (state_of_game.charAt(6) != '-' && state_of_game.charAt(6) == state_of_game.charAt(7) && state_of_game.charAt(7) == state_of_game.charAt(8)))
            return true;
        return false;
    }

}
