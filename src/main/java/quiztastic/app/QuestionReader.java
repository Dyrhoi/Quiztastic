package quiztastic.app;

import quiztastic.core.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * The Question Reader should read the questions from a file.
 */
public class QuestionReader {
    private final BufferedReader reader;

    public QuestionReader(BufferedReader reader) {
        this.reader = reader;
    }

    public QuestionReader(Reader reader) {
        this(new BufferedReader(reader));
    }

    public Question readQuestion() throws IOException, NumberFormatException {
        //throw new UnsupportedOperationException("Not yet implemented");
        String line = reader.readLine();
        if(line == null)
            return null;

        String[] questionData = line.split("\t");
        if(questionData.length != 4)
            return null;

        int score = Integer.parseInt(questionData[0]);
        String category = questionData[1];
        String question = questionData[2];
        String answer = questionData[3];

        return new Question(score, category, question, answer);
    }

    public BufferedReader getUnderlyingReader() {
        return reader;
    }
}
