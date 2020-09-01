package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ListQuestionRepository implements QuestionRepository {

    private final HashMap<Category, List<Question>> questionsRepo = new HashMap<>();
    public ListQuestionRepository(QuestionReader reader) throws IOException, ParseException {
        Question q;
        while((q = reader.readQuestion()) != null) {
            this.questionsRepo.computeIfAbsent(q.getCategory(), k -> new ArrayList<>()).add(q);
        }

    }

    public static ListQuestionRepository fromQuestionReader(QuestionReader reader) throws IOException, ParseException {
        return new ListQuestionRepository(reader);
    }

    @Override
    public List<Category> getCategories() {
        ArrayList<Category> cs = new ArrayList<>(this.questionsRepo.keySet());
        Collections.shuffle(cs);
        return new ArrayList<>(cs);
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        Collections.shuffle(this.questionsRepo.get(category));
        return new ArrayList<>(this.questionsRepo.get(category));
    }
}
