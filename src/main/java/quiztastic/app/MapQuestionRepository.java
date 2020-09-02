package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class MapQuestionRepository implements QuestionRepository {

    private final Map<Category, List<Question>> questionsByCategory;

    public MapQuestionRepository(Map<Category, List<Question>> questionsMapCategory)  {
        this.questionsByCategory = questionsMapCategory;
    }

    public static MapQuestionRepository fromQuestionReader(QuestionReader reader) throws IOException, ParseException {
        Map<Category, List<Question>> questionsMapCategory = new HashMap<>();
        Question q;
        while((q = reader.readQuestion()) != null) {
            questionsMapCategory.computeIfAbsent(q.getCategory(), k -> new ArrayList<>()).add(q);
        }
        return new MapQuestionRepository(questionsMapCategory);
    }

    @Override
    public List<Category> getCategories() {
        ArrayList<Category> cs = new ArrayList<>(this.questionsByCategory.keySet());
        Collections.shuffle(cs);
        return cs;
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        Collections.shuffle(this.questionsByCategory.get(category));
        return this.questionsByCategory.get(category);
    }
}
