package quiztastic.domain;

import quiztastic.core.Category;
import quiztastic.core.Question;

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

}
