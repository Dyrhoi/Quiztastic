package quiztastic.core;

import java.util.ArrayList;
import java.util.List;

/** A Jeopardy Board
 *
 */
public class Board {
    private final List<Group> groups;

    public Board(List<Group> groups) {
        this.groups = new ArrayList<>(groups);
        if(groups.size() != 6)
            throw new IllegalArgumentException("Board should contain 6 question groups, received  " + groups.size());
    }

    public static class Group {
        private final Category category;
        private final List<Question> questions;

        public Group(Category category, List<Question> questions) {
            this.category = category;
            this.questions = new ArrayList<>(questions);
            validate();

        }
        private void validate() {
            if(questions.size() != 5)
                throw new IllegalArgumentException("Board group should contain 5 questions, received  " + questions.size());

            for(Question q : questions) {
                if(q.getCategory() != this.category)
                    throw new IllegalArgumentException("Expected all question categories to be "
                            + this.category + " received" + q.getCategory());
            }
        }
    }
}
