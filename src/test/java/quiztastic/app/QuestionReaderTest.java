package quiztastic.app;

import org.junit.jupiter.api.Test;
import quiztastic.core.Question;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class QuestionReaderTest {

    @Test
    void shouldReadALine() throws IOException {
        BufferedReader br = new BufferedReader(new StringReader("Hello, World"));
        assertEquals(br.readLine(), "Hello, World");
    }

    @Test
    void shouldReadMultipleLines() throws IOException {
        BufferedReader br = new BufferedReader(new StringReader("Hello, World\nOther Line"));
        assertEquals(br.readLine(), "Hello, World");
        assertEquals(br.readLine(), "Other Line");
        assertNull(br.readLine());
    }

    @Test
    void shouldSetBufferedReader() {
        BufferedReader br = new BufferedReader(new StringReader("Hello, World\nOther Line"));
        QuestionReader qr = new QuestionReader(br);
        assertEquals(qr.getUnderlyingReader(), br);
    }

    @Test
    void shouldReadSingleQuestion() throws IOException {
        String questionText = "100\tLAKES & RIVERS\tRiver mentioned most often in the Bible\tthe Jordan\n";
        QuestionReader reader = new QuestionReader(new StringReader(questionText));
        Question q = reader.readQuestion();
        assertNotNull(q);
        // Insert more tests
        assertEquals(q.getScore(), 100);

        Question end = reader.readQuestion();
        assertNull(end);
    }

    @Test
    void shouldReadManyQuestions() throws IOException {
        InputStream s = this.getClass()
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        if (s == null) fail();

        QuestionReader reader = new QuestionReader(
                new InputStreamReader(s));
        int count = 0;
        while (reader.readQuestion() != null) {
            count++;
        }

        assertEquals(13, count);
    }

}