package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class MapQuestionRepository implements QuestionRepository {

    private final HashMap<Category, List<Question>> questionsRepo = new HashMap<>();

    public MapQuestionRepository(QuestionReader reader) throws IOException, ParseException {
        Question q;
        while((q = reader.readQuestion()) != null) {
            this.questionsRepo.computeIfAbsent(q.getCategory(), k -> new ArrayList<>()).add(q);
        }
    }

    public static MapQuestionRepository fromQuestionReader(QuestionReader reader) throws IOException, ParseException {
        return new MapQuestionRepository(reader);
    }

    @Override
    public List<Category> getCategories() {
        ArrayList<Category> cs = new ArrayList<>(this.questionsRepo.keySet());
        Collections.shuffle(cs);
        return cs;
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        Collections.shuffle(this.questionsRepo.get(category));
        return this.questionsRepo.get(category);
    }
}
