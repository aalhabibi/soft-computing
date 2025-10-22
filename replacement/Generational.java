package replacement;

import core.Population;

public class Generational<T> implements ReplacementStrategy<T> {
    @Override
    public Population<T> replace(Population<T> oldPop, Population<T> newPop) {
        // Full replacement — new population completely replaces old one
        return newPop;
    }
}
