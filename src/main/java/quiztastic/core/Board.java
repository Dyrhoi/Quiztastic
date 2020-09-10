package quiztastic.core;

import java.util.ArrayList;
import java.util.List;

/** A Jeopardy Board
 *
 */
public class Board {
    private final List<Group> groups;

    public Board(List<Group> groups) {
        this.groups = List.copyOf(groups);
        if (this.groups.size() != 6) {
            throw new IllegalArgumentException(
                    "Should be 6 groups, there were " + groups.size());
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "Board{" +
                "groups=" + groups +
                '}';
    }

    public Question getQuestionByScore(int groupId, int score) {
        Group group = groups.get(groupId);
        for(Question q : group.getQuestions()) {
            if (q.getScore() == score) return q;
        }
        return null;
    }

    public static class Group {
        private final Category category;
        private final List<Question> questions;

        public Group(Category category, List<Question> questions) {
            this.category = category;
            this.questions = List.copyOf(questions);
            //this.questions = new ArrayList<>(questions);
            validate();
        }

        private void validate() {
            if (questions.size() != 5) {
                throw new IllegalArgumentException(
                        "Should be 5 groups, there were " + questions.size());
            }
            for (Question q : questions) {
                if (!q.getCategory().equals(category)) {
                    throw new IllegalArgumentException("Expected all categories to be "
                            + category + " but was " + q.getCategory());
                }
            }
        }

        public Category getCategory() {
            return category;
        }

        public List<Question> getQuestions() {
            return questions;
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
