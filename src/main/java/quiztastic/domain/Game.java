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

    public String answerQuestion(int categoryNumber, int questionNumber, String answer) {
        Question q = this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
        answerList.add(new Answer(categoryNumber, questionNumber, answer));
        if(q.getAnswer().equals(answer)) {
            return null;
        }
        return q.getAnswer();
    }

    public boolean isAnswered(int categoryNumber, int questionNumber) {
        for (Answer a : answerList) {
            if(a.hasIndex(categoryNumber, questionNumber)) {
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
        private final int questionNumber;
        private final String answer;

        public Answer(int categoryNumber, int questionNumber, String answer) {
            this.categoryNumber = categoryNumber;
            this.questionNumber = questionNumber;
            this.answer = answer;
        }

        public boolean hasIndex(int categoryNumber, int questionNumber) {
            return (this.categoryNumber == categoryNumber && this.questionNumber == questionNumber);
        }
    }
}
