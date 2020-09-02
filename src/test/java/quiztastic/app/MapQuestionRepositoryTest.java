package quiztastic.app;

import org.junit.jupiter.api.Test;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class MapQuestionRepositoryTest {

    Path pathToSmallQuestionFile() {
        URL url = this.getClass()
                .getClassLoader()
                .getResource("questions-small.tsv");
        if (url == null) fail();
        return Path.of(url.getFile());
    }

    public static MapQuestionRepository getQuestionsSmallRepo() throws IOException, ParseException {
        InputStream s = MapQuestionRepository.class
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        return MapQuestionRepository.fromQuestionReader(
                new QuestionReader(new InputStreamReader(s)));
    }

    @Test
    void shouldReadTheSmallQuestionFile() throws IOException, ParseException {
        QuestionRepository repo = getQuestionsSmallRepo();
        // Perform tests of equality
    }

}