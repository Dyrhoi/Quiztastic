package quiztastic.entries;

import quiztastic.app.QuestionReader;
import quiztastic.core.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

public class DisplayQuestions {

    public void displayQuestions () throws IOException, ParseException {
        InputStream s = this.getClass()
                .getClassLoader()
                .getResourceAsStream("master_season1-35clean.tsv");

        QuestionReader reader = new QuestionReader(new InputStreamReader(s));
        int count = 0;
        Question q;
        while ((q = reader.readQuestion()) != null) {
            System.out.println(q);
        }


    }

    public static void main(String[] args) throws IOException, ParseException {
        new DisplayQuestions().displayQuestions();
    }
}
