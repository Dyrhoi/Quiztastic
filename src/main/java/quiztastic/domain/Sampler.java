package quiztastic.domain;

import quiztastic.core.Question;

import java.util.List;

public interface Sampler {
    List<Question> sample(List<Question> questions, int amount);
}
