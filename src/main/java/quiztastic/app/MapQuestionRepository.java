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

            //Singleton Pattern, only add element to Question List if it exists.
            List<Question> questionList = questionsMapCategory.get(q.getCategory());
            if(questionList == null) {
                Category category = q.getCategory();
                questionList = new ArrayList<>();
                questionsMapCategory.put(category, questionList);
            }
            questionList.add(q);

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
        Map<Integer, Question> questionsUniqueScore = new HashMap<>();
        for(Question q : this.questionsByCategory.get(category)) {
            questionsUniqueScore.put(q.getScore(), q);
        }
        return new ArrayList<>(questionsUniqueScore.values());
    }

    @Override
    public Iterable<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for(List<Question> l : this.questionsByCategory.values()) {
            questions.addAll(l);
        }
        return questions;
    }
}
