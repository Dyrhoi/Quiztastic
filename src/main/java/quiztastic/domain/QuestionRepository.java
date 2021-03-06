package quiztastic.domain;

import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public interface QuestionRepository {

    /**
     *
     * @return a potentially random list of categories available
     */
    List<Category> getCategories();

    /**
     *
     * @return a list of questions for a given category.
     */
    List<Question> getQuestionsWithCategory(Category category);

    /**
     *
     * @return a potentially random iterable of all known questions.
     */
    Iterable<Question> getQuestions();
}
