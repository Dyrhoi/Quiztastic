package quiztastic.app;

import org.junit.jupiter.api.Test;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ListQuestionRepositoryTest {

    Path pathToSmallQuestionFile() {
        URL url = this.getClass()
                .getClassLoader()
                .getResource("questions-small.tsv");
        if (url == null) fail();
        return Path.of(url.getFile());
    }

    public ListQuestionRepository getQuestionsSmallRepo() {
        InputStream s = this.getClass()
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        if (s == null) fail();
        return ListQuestionRepository.fromQuestionReader(
                new QuestionReader(new InputStreamReader(s)));
    }

    @Test
    void shouldReadTheSmallQuestionFile() throws IOException {
        QuestionReader reader = new QuestionReader(
                Files.newBufferedReader(pathToSmallQuestionFile()));
        QuestionRepository repo =
                ListQuestionRepository.fromQuestionReader(reader);
        repo.getQuestion(0);
        // Perform tests of equality
    }

}