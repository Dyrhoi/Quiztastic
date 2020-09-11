package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;

public class Protocol implements Runnable {
    private final Quiztastic quiz;
    private final Scanner in;
    private final PrintWriter out;
    private Socket socket;

    public Protocol(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;

        this.quiz = Quiztastic.getInstance();
    }

    public Protocol(Scanner in, PrintWriter out, Socket socket) {
        this(in, out);
        this.socket = socket;
    }

    private final Map<Integer, String> IDToAlphabet = Map.of(
            0,"A",
            1, "B",
            2, "C",
            3, "D",
            4, "E",
            5, "F"
    );

    private String fetchInput() {
        out.print("> ");
        out.flush();
        return in.nextLine().strip().toLowerCase();
    }

    private String fetchCmd(String input) {
        return input.split(" ")[0] != null ? input.split(" ")[0] : input;
    }

    private String[] fetchArgs(String cmd, String line) {
        return line.split(" ").length > 1 ? line.substring(cmd.length() + 1, line.length() - cmd.length() + 1).split( " ") : new String[] {};
    }

    //private String
    @Override
    public void run() {
        String line = fetchInput();
        String cmd = fetchCmd(line);
        String[] args = fetchArgs(cmd, line);
        while(!cmd.equals("quit")) {
            System.out.println("command executed: " + cmd);
            switch(cmd) {
                case "h":
                case "help":
                    displayHelp();
                    break;
                case "d":
                case "draw":
                    displayBoard();
                    break;
                case "a":
                case "answer":
                    answerQuestion(args);
                    break;
                default:
                    out.println("Unrecognized command: " + line);
            }
            out.flush();
            line = fetchInput();
            cmd = fetchCmd(line);
            args = fetchArgs(cmd, line);
        }
        //TODO: Remove socket:
        //code here.
        try {
            if(socket != null) {
                socket.close();
                System.out.println(socket.getPort() + " disconnected.");
            }
        } catch (IOException e) {
            System.out.println("Tried to disconnect " + socket.getPort() + " but encountered an error: "  + e.getMessage());
        }
    }

    public void displayHelp() {
        out.println(
                "Your options are: \n\r" +
                "  - [h]elp: ask for help.\n\r" +
                "  - [d]raw: draw the board.\n\r" +
                "  - [a]nswer A200: get the question for category A, question 200.\n\r" +
                "  - [q]uit: exit program."
        );
    }

    public void displayBoard() {
        int fieldDividerLength = 4;

        Board board = this.quiz.getCurrentGame().getBoard();
        List<Category> categories = this.quiz.getCurrentGame().getCategories();
        Map<Integer, List<Question>> rowQuestions = new HashMap<>();

        for(Board.Group group : board.getGroups()) {

            //Immutable object, create copy.
            ArrayList<Question> groupQuestions = new ArrayList<>(group.getQuestions());
            groupQuestions.sort(Comparator.comparingInt(Question::getScore));
            int x = 0;
            for(Question q : groupQuestions) {
                rowQuestions.computeIfAbsent(x, k -> new ArrayList<>()).add(q);
                x++;
            }
        }

        // Print all categories, and measure the "length" between them in UI.
        int[] fieldWidths = new int[6];
        int i = 0;
        for(Category cat : categories) {
            String name = IDToAlphabet.get(i) + ". " + cat.getName();
            out.printf("%s%" + fieldDividerLength + "s", name, "");
            fieldWidths[i] = name.length();
            i++;
        }

        // Add separator with the width of all the categories. ---
        out.println();
        int whitespace = fieldDividerLength * categories.size();
        for(int x = 0; x < (IntStream.of(fieldWidths).sum() + whitespace); x++) {
            out.printf("-");
        }
        out.println();

        //Insert all questions
        for(List<Question> qs : rowQuestions.values()) {
            int x = 0;
            for (Question q : qs) {
                if(this.quiz.getCurrentGame().isAnswered(x, q.getScore()))
                    out.printf("%-" + fieldWidths[x] + "s%" + fieldDividerLength + "s", "---", "");
                else
                    out.printf("%-" + fieldWidths[x] + "d%" + fieldDividerLength + "s", q.getScore(), "");
                x++;
            }
            out.println();
        }

    }

    public void answerQuestion(String[] args) {
        if(args.length < 1) {
            out.println("Error not enough arguments.");
            return;
        }
        List<String> list = new ArrayList<>();
        for(int i : IDToAlphabet.keySet()) {
            list.add(IDToAlphabet.get(i).toLowerCase());
        }
        Collections.sort(list);
        int categoryID = String.join("", list).indexOf(args[0].substring(0,1));
        int score = -1;
        try {
            score = Integer.parseInt(args[0].substring(1));
        } catch (NumberFormatException e) {
            out.println("Couldn't read the question score");
            return;
        }
        // Debug category number + score pick: out.println(categoryID + " " + score);

        Question foundQuestion = this.quiz.getCurrentGame().getBoard().getQuestionByScore(categoryID, score);
        if(foundQuestion == null) {
            out.println("Error, question wasn't found on board");
            return;
        }
        if(this.quiz.getCurrentGame().isAnswered(categoryID, score)) {
            out.println("This question has already been tried.");
            return;
        }

        out.println(foundQuestion.getQuestion());

        String inAnswer = in.nextLine();
        if(this.quiz.getCurrentGame().isAnswered(categoryID, score)) {
            out.println("Too slow! This question was just been tried.");
            return;
        }
        String correctAnswer = this.quiz.getCurrentGame().answerQuestion(categoryID, score, inAnswer);
        if(correctAnswer == null) {
            out.println("Correct answer! Awarded: " + score + " points!");
            //TODO: keep track of points.
        }
        else {
            out.println("Incorrect answer, the correct was: " + correctAnswer);
        }
    }
}
