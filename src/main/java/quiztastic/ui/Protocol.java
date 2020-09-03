package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;

import java.io.PrintWriter;
import java.util.Scanner;

public class Protocol {
    private final Quiztastic quiz;
    private final Scanner in;
    private final PrintWriter out;

    public Protocol(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;

        this.quiz = Quiztastic.getInstance();
    }

    private String fetchCommand() {
        out.print("> ");
        out.flush();
        return in.nextLine().strip().toLowerCase();
    }

    public void run() {
        String line = fetchCommand();
        while(!line.equals("quit")) {
            switch(line) {
                case "h":
                case "help":
                    displayHelp();
                    break;
                case "d":
                case "draw":
                    displayBoard();
                    break;
                default:
                    out.println("Unrecognized command: " + line);
            }
            out.flush();
            line = fetchCommand();
        }
    }

    public void displayHelp() {
        out.println(
                "Your options are: \n" +
                "  - [h]elp: ask for help.\n" +
                "  - [d]raw: draw the board.\n" +
                "  - [a]nswer A200: get the question for category A, question 200.\n" +
                "  - [q]uit: exit program."
        );
    }

    public void displayBoard() {
        /*
        *
        * TODO: Getters for our Board groups, so we can manipulate the data.
        *  Display the formatted board.
        *
        * */
    }
}
