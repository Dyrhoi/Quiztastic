package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoardController {
    public final QuestionRepository questionRepository;

    public BoardController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Board.Group makeGroup(Category c) throws IllegalArgumentException {
        List<Question> questions =
                questionRepository.getQuestionsWithCategory(c);
        if (questions.size() >= 5) {
            return new Board.Group(c, questions.subList(0, 5));
        } else {
            throw new IllegalArgumentException("Not enough questions in category");
        }
    }

    public Board makeBoard() {
        List<Board.Group> groups = new ArrayList<>();
        for (Category c : questionRepository.getCategories()) {
            if(groups.size() >= 6) break;
            try {
                groups.add(makeGroup(c));
            } catch (IllegalArgumentException e) {
                //System.err.println(e.getMessage());
            }
        }
        return new Board(groups);
    }
}
