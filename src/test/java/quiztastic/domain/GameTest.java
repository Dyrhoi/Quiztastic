package quiztastic.domain;

import com.cedarsoftware.util.StringUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void answerQuestion() {
        String uAnswer = "cat";
        String answer = "kat";
        answer = answer.toLowerCase();
        uAnswer = uAnswer.toLowerCase();
        int levenshteinDistance = StringUtilities.levenshteinDistance(uAnswer, answer);
        int acceptedDistance = (int) Math.round(answer.length() * .15);

        if(levenshteinDistance <= acceptedDistance) {
            System.out.println("Correct, ld: " + levenshteinDistance + " accepted distance: " + acceptedDistance);
        }
        else {
            System.out.println("Incorrect, ld: " + levenshteinDistance + " accepted distance: " + acceptedDistance);
        }
    }
}