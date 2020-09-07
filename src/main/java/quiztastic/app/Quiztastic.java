package quiztastic.app;

import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.domain.BoardFactory;
import quiztastic.domain.Game;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

public class Quiztastic {
    private static Quiztastic instance;

    public static Quiztastic getInstance() {
        if(instance == null) {
            InputStream s = Quiztastic.class
                    .getClassLoader()
                    .getResourceAsStream("master_season1-35clean.tsv");
            QuestionReader reader = new QuestionReader(new InputStreamReader(s));
            try {
                MapQuestionRepository repo = MapQuestionRepository.fromQuestionReader(reader);
                Game game = new Game(new BoardFactory(repo).makeBoard(), new ArrayList<>());
                instance = new Quiztastic(repo, game);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    private final QuestionRepository questions;
    private final Game game;

    private Quiztastic(QuestionRepository questions, Game game) {
        this.questions = questions;
        this.game = game;
    }

    /* API BEGIN */

    public Iterable<Question> getQuestions() {
        return this.questions.getQuestions();
    }

    public Board getBoard() {
       return this.game.getBoard();
    }

    public Game getCurrentGame() {
        return game;
    }
}
