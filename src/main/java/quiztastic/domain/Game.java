package quiztastic.domain;

import com.cedarsoftware.util.StringUtilities;
import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private final Board board;
    private final List<Answer> answerList;
    private final Map<Player, Integer> playersScore;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
        this.playersScore = new HashMap<>();
    }

    public Player addPlayer(String name) {
        Player player = new Player(name);
        this.playersScore.put(player, 0);
        return player;
    }

    public void removePlayer(Player player) {
        this.playersScore.remove(player);
    }

    public Map<Player, Integer> getPlayers() {
        return playersScore;
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

        System.out.println(answer);
        System.out.println(levenshteinDistance + " : " + acceptedDistance);

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
