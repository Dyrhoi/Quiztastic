package quiztastic.app;

import org.junit.jupiter.api.Test;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

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
    void shouldReadSingleQuestion() throws IOException, ParseException {
        String questionText = "100\tLAKES & RIVERS\tRiver mentioned most often in the Bible\tthe Jordan\n";
        QuestionReader reader = new QuestionReader(new StringReader(questionText));
        Question q = reader.readQuestion();
        assertNotNull(q);
        // Insert more tests
        assertEquals(100, q.getScore());
        assertEquals(new Category("LAKES & RIVERS"), q.getCategory());
        assertEquals("River mentioned most often in the Bible", q.getQuestion());
        assertEquals("the Jordan", q.getAnswer());

        Question end = reader.readQuestion();
        assertNull(end);
    }

    @Test
    void shouldThrowParseExceptionTooFewFields() {
        String questionText = "100\tLAKES & RIVERS\tthe Jordan\n";
        QuestionReader qr = new QuestionReader(new StringReader(questionText));
        ParseException e = assertThrows(ParseException.class, () -> {
            qr.readQuestion();
        });
        assertEquals("Expected 4 fields, received: 3", e.getMessage());
    }

    @Test
    void shouldThrowParseExceptionBadInteger() {
        String questionText = "xxx\tLAKES & RIVERS\tQuestion\tthe Jordan\n";
        QuestionReader qr = new QuestionReader(new StringReader(questionText));
        ParseException e = assertThrows(ParseException.class, () -> {
            qr.readQuestion();
        });
        assertEquals("Expected int in field 1, but received \"xxx\"", e.getMessage());
    }

    @Test
    void shouldReadManyQuestions() throws IOException, ParseException {
        InputStream s = this.getClass()
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        if (s == null) fail();

        QuestionReader reader = new QuestionReader(new InputStreamReader(s));
        int count = 0;
        while (reader.readQuestion() != null) {
            count++;
        }

        assertEquals(13, count);
    }

}