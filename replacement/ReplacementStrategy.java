package replacement;

import core.Population;

public interface ReplacementStrategy<T> {
    Population<T> replace(Population<T> oldPop, Population<T> newPop);
}
