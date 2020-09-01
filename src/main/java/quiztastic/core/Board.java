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
        if (this.groups.size() != 6) {
            throw new IllegalArgumentException(
                    "Should be 6 groups, there were " + groups.size());
        }
    }

    @Override
    public String toString() {
        return "Board{" +
                "groups=" + groups +
                '}';
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
            if (questions.size() != 5) {
                throw new IllegalArgumentException(
                        "Should be 5 groups, there were " + questions.size());
            }
            for (Question q : questions) {
                if (q.getCategory() != category) {
                    throw new IllegalArgumentException("Expected all categories to be "
                            + category + " but was " + q.getCategory());
                }
            }
        }

        @Override
        public String toString() {
            return "Group{" +
                    "category=" + category +
                    ", questions=" + questions +
                    '}';
        }
    }
}
