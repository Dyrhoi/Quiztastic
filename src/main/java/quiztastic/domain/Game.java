package quiztastic.domain;

import com.cedarsoftware.util.StringUtilities;
import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.ui.Protocol;

import java.util.*;

public class Game {

    private final Board board;
    private final List<Answer> answerList;
    private final ArrayList<Protocol> clients;
    private volatile Player currentPlayer;
    private volatile Map<Player, Integer> playersScore;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
        this.playersScore = new LinkedHashMap<>();
        this.clients = new ArrayList<>();

        this.currentPlayer = null;
    }

    public synchronized Player nextPlayer() {
        List<Player> keys = new ArrayList<>(playersScore.keySet());
        Player nextPlayer;
        try {
             nextPlayer = keys.get(keys.indexOf(this.currentPlayer) + 1);
        } catch(IndexOutOfBoundsException e) {
            try {
                nextPlayer = keys.get(0);
            } catch (IndexOutOfBoundsException e2) {
                nextPlayer = null;
            }
        }
        this.currentPlayer = nextPlayer;
        if(playersScore.size() > 0) broadcastNextTurn();

        return nextPlayer;
    }

    private void broadcastNextTurn() {
        for (Protocol client : clients) {
            client.getOut().println("It's " + currentPlayer + " turn to choose a Question!");
        }
    }

    private synchronized void forceCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public synchronized Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public synchronized Player addPlayer(String name) {
        Player player = new Player(name);

        //Set currentplayer if first player connected.
        if(currentPlayer == null)
            forceCurrentPlayer(player);

        this.playersScore.put(player, 0);
        return player;
    }

    public synchronized void removePlayer(Player player) {
        this.playersScore.remove(player);
        if(currentPlayer == player)
            nextPlayer();
    }


    public Map<Player, Integer> getPlayers() {
        return playersScore;
    }

    public synchronized void addClient(Protocol client) {
        this.clients.add(client);
    }

    public synchronized void removeClient(Protocol client) {
        this.clients.remove(client);
    }

    public int addScore(Player player, int score) {
        this.playersScore.put(player, this.playersScore.get(player) + score);
        return this.playersScore.get(player);
    }

    public List<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        for(Board.Group g : this.board.getGroups()) {
            categories.add(g.getCategory());
        }
        return categories;
    }

    public String answerQuestion(int categoryNumber, int questionScore, String answer) {
        Question q = this.board.getQuestionByScore(categoryNumber, questionScore);
        answerList.add(new Answer(categoryNumber, questionScore, answer));

        int levenshteinDistance = StringUtilities.levenshteinDistance(q.getAnswer().toLowerCase(), answer);
        int acceptedDistance = (int) Math.round(answer.length() * .15);

        if(levenshteinDistance <= acceptedDistance) { //correct answer

            return null;

        }
        return q.getAnswer();
    }

    public boolean isAnswered(int categoryNumber, int questionScore) {
        for (Answer a : answerList) {
            if(a.hasIndex(categoryNumber, questionScore)) {
                return true;
            }
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    public synchronized void kickAll() {
        for (Protocol client : clients) {
            this.removePlayer(client.getPlayer());
            client.setRunning(false);
        }
    }

    private class Answer {
        private final int categoryNumber;
        private final int questionScore;
        private final String answer;

        public Answer(int categoryNumber, int questionScore, String answer) {
            this.categoryNumber = categoryNumber;
            this.questionScore = questionScore;
            this.answer = answer;
        }

        public boolean hasIndex(int categoryNumber, int questionScore) {
            return (this.categoryNumber == categoryNumber && this.questionScore == questionScore);
        }
    }
}
