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

    public static ListQuestionRepository getQuestionsSmallRepo() {
        InputStream s = ListQuestionRepository.class
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        return ListQuestionRepository.fromQuestionReader(
                new QuestionReader(new InputStreamReader(s)));
    }

    @Test
    void shouldReadTheSmallQuestionFile() throws IOException {
        QuestionRepository repo = getQuestionsSmallRepo();
        // Perform tests of equality
    }

}