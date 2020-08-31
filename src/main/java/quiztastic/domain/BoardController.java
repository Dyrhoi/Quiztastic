package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.util.List;

public class BoardController {
    public final QuestionRepository qRepo;
    public final Sampler sampler;

    public BoardController(QuestionRepository qRepo, Sampler sampler) {
        this.qRepo = qRepo;
        this.sampler = sampler;
    }

    public Board.Group makeGroup(Category category) {
        List<Question> questionList = qRepo.getQuestionsWithCategory(category);
        List<Question> sampledQuestion = sampler.sample(questionList, 5);
        return new Board.Group(category, sampledQuestion);
    }

    public Board makeBoard() {
        return null;
    }
}
