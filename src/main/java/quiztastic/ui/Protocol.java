package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

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
        Board board = this.quiz.getBoard();
        List<Category> categories = new ArrayList<>();
        Map<Integer, List<Question>> rowQuestions = new HashMap<>();
        for(Board.Group group : board.getGroups()) {
            categories.add(group.getCategory());

            group.getQuestions().sort(Comparator.comparingInt(Question::getScore));
            int x = 0;
            for(Question q : group.getQuestions()) {
                rowQuestions.computeIfAbsent(x, k -> new ArrayList<>()).add(q);
                x++;
            }
        }

        // Print all categories, and measure the "length" between them in UI.
        int[] fieldWidths = new int[6];
        int i = 0;
        for(Category cat : categories) {
            out.print(cat.getName() + "    ");
            fieldWidths[i] = cat.getName().length();
            i++;
        }

        // Add separator with the width of all the categories. ---
        out.println();
        for(int x = 0; x < IntStream.of(fieldWidths).sum() + (4 * 5); x++) {
            out.printf("-");
        }
        out.println();

        //Insert all questions
        for(List<Question> qs : rowQuestions.values()) {
            int x = 0;
            for (Question q : qs) {
                out.printf("%-" + fieldWidths[x] + "d    ", q.getScore());
                x++;
            }
            out.println();
        }
    }
}
