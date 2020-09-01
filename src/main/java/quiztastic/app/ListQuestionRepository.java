package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.util.Iterator;
import java.util.List;

public class ListQuestionRepository implements QuestionRepository {

    public static ListQuestionRepository fromQuestionReader(QuestionReader reader) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public List<Category> getCategories() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
