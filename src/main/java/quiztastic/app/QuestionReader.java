package quiztastic.app;

import quiztastic.core.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

/**
 * The Question Reader should read the questions from a file.
 */
public class QuestionReader {
    private final BufferedReader reader;
    private int lineCounter;

    public QuestionReader(BufferedReader reader) {
        this.reader = reader;
    }

    public QuestionReader(Reader reader) {
        this(new BufferedReader(reader));
    }

    public Question readQuestion() throws IOException, ParseException, NumberFormatException {
        //throw new UnsupportedOperationException("Not yet implemented");
        lineCounter++;

        String line = reader.readLine();
        if(line == null)
            return null;

        String[] questionFields = line.split("\t");
        if(questionFields.length != 4)
            throw new ParseException("Expected 4 fields, received: " + questionFields.length, lineCounter);

        int score;
        try {
            score = Integer.parseInt(questionFields[0]);
        }
        catch (NumberFormatException e) {
            throw new ParseException("Expected int in field 1, but received \"" + questionFields[0] + "\"",
                    lineCounter);
        }
        String category = questionFields[1];
        String question = questionFields[2];
        String answer = questionFields[3];

        return new Question(score, category, question, answer);
    }

    public BufferedReader getUnderlyingReader() {
        return reader;
    }
}
