package geneticAlgorithm.replacement;

import geneticAlgorithm.core.Population;

public class Generational<T> implements ReplacementStrategy<T> {
    @Override
    public Population<T> replace(Population<T> oldPop, Population<T> newPop) {
        // new population completely replaces old one
        return newPop;
    }
}
