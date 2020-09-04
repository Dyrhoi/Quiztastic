package quiztastic.app;

import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.domain.BoardFactory;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

public class Quiztastic {
    private static Quiztastic instance;

    public static Quiztastic getInstance() {
        if(instance == null) {
            InputStream s = Quiztastic.class
                    .getClassLoader()
                    .getResourceAsStream("master_season1-35clean.tsv");
            QuestionReader reader = new QuestionReader(new InputStreamReader(s));
            try {
                instance = new Quiztastic(MapQuestionRepository.fromQuestionReader(reader));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    private final QuestionRepository questions;
    private final Board board;

    private Quiztastic(QuestionRepository questions) {
        this.questions = questions;
        this.board = new BoardFactory(this.questions).makeBoard();
    }

    /* API BEGIN */

    public Iterable<Question> getQuestions() {
        return this.questions.getQuestions();
    }

    public Board getBoard() {
       return this.board;
    }
}
