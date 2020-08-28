package quiztastic.core;

/**
 * The Question Class.
 *
 * Should contain information about the questions
 */
public class Question {
    private final int score;
    private final String category;
    private final String question;
    private final String answer;

    public Question(int score, String category, String question, String answer) {
        this.score = score;
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }
}
