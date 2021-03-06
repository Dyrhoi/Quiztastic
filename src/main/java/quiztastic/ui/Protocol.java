package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.Game;
import quiztastic.domain.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;

public class Protocol implements Runnable {
    private final Quiztastic quiz;
    private final Scanner in;
    private final PrintWriter out;
    private final Game game;
    private Player player;
    private boolean running = true;

    public Protocol(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;

        this.quiz = Quiztastic.getInstance();
        this.game = this.quiz.getCurrentGame();
    }

    private final Map<Integer, String> IDToAlphabet = Map.of(
            0, "A",
            1, "B",
            2, "C",
            3, "D",
            4, "E",
            5, "F"
    );

    private String fetchInput(String symbol) {
        try {
            out.print(symbol + " ");
            out.flush();
            return in.nextLine().strip().toLowerCase();
        } catch (Exception e) {
            System.out.println("A player disconnected unexpectedly, removing player");
            this.game.removePlayer(player);
            this.game.removeClient(this);
            return null;
        }
    }

    /*
    private String fetchAnswer() {
        out.print("? ");
        out.flush();
        return in.nextLine().strip().toLowerCase();
    }
     */

    private String fetchCmd(String input) {
        return input.split(" ")[0] != null ? input.split(" ")[0] : input;
    }

    private String[] fetchArgs(String cmd, String line) {
        return line.split(" ").length > 1 ? line.substring(cmd.length() + 1, line.length() - cmd.length() + 1).split(" ") : new String[]{};
    }

    private void initPlayer() {
        out.println("Velkommen til Quiztastic, indtast dit navn:");
        String name = fetchInput(">");
        this.player = this.game.addPlayer(name);
    }


    @Override
    public void run() {
        this.game.addClient(this);
        initPlayer();

        String line = fetchInput(">");
        String cmd = fetchCmd(line);
        String[] args = fetchArgs(cmd, line);
        while (!cmd.equals("quit") && running) {
            System.out.println("command executed: " + cmd);
            switch (cmd) {
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
                case "s":
                case "score":
                    getScores();
                    break;
                case "forcekick":
                    this.game.kickAll();
                    break;
                case "debug":
                    debug(args);
                    break;
                default:
                    out.println("Unrecognized command: " + line);
            }
            out.flush();
            line = fetchInput(">");
            cmd = fetchCmd(line);
            args = fetchArgs(cmd, line);
        }
        this.game.removePlayer(player);
        this.game.removeClient(this);
    }

    public void displayHelp() {
        out.println(
                "Your options are: \n\r" +
                        "  - [h]elp: ask for help.\n\r" +
                        "  - [d]raw: draw the board.\n\r" +
                        "  - [a]nswer A200: get the question for category A, question 200.\n\r" +
                        "  - [s]core: see everyone's score.\n\r" +
                        "  - quit: exit program."
        );
    }

    public void displayBoard() {
        int fieldDividerLength = 4;

        Board board = this.game.getBoard();
        List<Category> categories = this.game.getCategories();
        Map<Integer, List<Question>> rowQuestions = new HashMap<>();

        for (Board.Group group : board.getGroups()) {

            //Immutable object, create copy.
            ArrayList<Question> groupQuestions = new ArrayList<>(group.getQuestions());
            groupQuestions.sort(Comparator.comparingInt(Question::getScore));
            int x = 0;
            for (Question q : groupQuestions) {
                rowQuestions.computeIfAbsent(x, k -> new ArrayList<>()).add(q);
                x++;
            }
        }

        // Print all categories, and measure the "length" between them in UI.
        int[] fieldWidths = new int[6];
        int i = 0;
        for (Category cat : categories) {
            String name = IDToAlphabet.get(i) + ". " + cat.getName();
            out.printf("%s%" + fieldDividerLength + "s", name, "");
            fieldWidths[i] = name.length();
            i++;
        }

        // Add separator with the width of all the categories. ---
        out.println();
        int whitespace = fieldDividerLength * categories.size();
        for (int x = 0; x < (IntStream.of(fieldWidths).sum() + whitespace); x++) {
            out.printf("-");
        }
        out.println();

        //Insert all questions
        for (List<Question> qs : rowQuestions.values()) {
            int x = 0;
            for (Question q : qs) {
                if (this.game.isAnswered(x, q.getScore()))
                    out.printf("%-" + fieldWidths[x] + "s%" + fieldDividerLength + "s", "---", "");
                else
                    out.printf("%-" + fieldWidths[x] + "d%" + fieldDividerLength + "s", q.getScore(), "");
                x++;
            }
            out.println();
        }

    }

    public void answerQuestion(String[] args) {

        /*

        Check arguments length (did user enter a category), and check if it's the players
        turn to choose category.

         */

        if (args.length < 1) {
            out.println("Error not enough arguments.");
            return;
        }

        if(this.game.getCurrentPlayer() != player) {
            out.println("Oops, it isn't your turn to choose category. It is: "
                    + this.game.getCurrentPlayer() + "'s turn");
            return;
        }

         /*

        Redefine arguments into category id and question choice.

         */

        List<String> list = new ArrayList<>();
        for (int i : IDToAlphabet.keySet()) {
            list.add(IDToAlphabet.get(i).toLowerCase());
        }
        Collections.sort(list);
        int categoryID = String.join("", list).indexOf(args[0].substring(0, 1));
        int score;
        try {
            score = Integer.parseInt(args[0].substring(1));
        } catch (NumberFormatException e) {
            out.println("Couldn't read the question score");
            return;
        }

        /*

        Find our chosen question and check if it's already been answered.

        */
        Question foundQuestion = this.game.getBoard().getQuestionByScore(categoryID, score);
        if (foundQuestion == null) {
            out.println("Error, question wasn't found on board");
            return;
        }
        if (this.game.isAnswered(categoryID, score)) {
            out.println("This question has already been tried.");
            return;
        }

        out.println(foundQuestion.getQuestion());

        /*

        Fetch our users answer, and check if it's been answered while prompted.

        */

        String inAnswer = fetchInput("?");
        if (this.game.isAnswered(categoryID, score)) {
            out.println("Too slow! This question was just been tried.");
            return;
        }

        /*

        Check if correct answer, award points.

         */
        String correctAnswer = this.game.answerQuestion(categoryID, score, inAnswer);
        if (correctAnswer == null) {
            out.println("Correct answer! Awarded: " + score + " points!");
            int newScore = this.game.addScore(player, score);
            out.println("You now have: " + newScore + " points!");
        } else {
            out.println("Incorrect answer, the correct was: " + correctAnswer);
        }

        this.game.nextPlayer();
    }

    public void getScores() {

        StringBuilder scoreText = new StringBuilder();

        for (Map.Entry<Player, Integer> entry : this.game.getPlayers().entrySet()) {
            Player player = entry.getKey();
            int score = entry.getValue();

            scoreText.append(player.getName()).append(" ").append(score).append(", ");
        }
        out.println(scoreText.substring(0, scoreText.length() - 2));
    }

    public void debug(String[] args) {
        out.println("Players connected:");
        out.println(this.game.getPlayers());
    }

    public PrintWriter getOut() {
        return out;
    }

    public Player getPlayer() {
        return player;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
