package quiztastic.ui;

import quiztastic.app.Quiztastic;

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
        return in.nextLine().strip();
    }

    public void run() {
        String line = fetchCommand();
        while(!line.equals("quit")) {
            switch(line) {
                case "h":
                case "help":
                    out.println("Help Command Issued.");
                    break;
                default:
                    out.println("Unrecognized command: " + line);
            }
            out.flush();
            line = fetchCommand();
        }
    }
}
