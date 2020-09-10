package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Board board;
    private final List<Answer> answerList;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
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
        if(q.getAnswer().equalsIgnoreCase(answer)) {
            return null;
        }
        return q.getAnswer();
    }
    //100
    //500

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
