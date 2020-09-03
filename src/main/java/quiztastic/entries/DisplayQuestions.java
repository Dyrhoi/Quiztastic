package quiztastic.entries;

import quiztastic.app.QuestionReader;
import quiztastic.app.Quiztastic;
import quiztastic.core.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

public class DisplayQuestions {

    public static void main(String[] args) {
        Quiztastic quiz = Quiztastic.getInstance();
        for(Question q : quiz.getQuestions()) {
            System.out.println(q);
        }
    }
}
