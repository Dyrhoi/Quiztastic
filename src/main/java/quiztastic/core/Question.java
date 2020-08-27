package quiztastic.core;

/**
 * The Question Class.
 *
 * Should contain information about the questions
 */
public class Question {
    private final int score;

    public Question(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
