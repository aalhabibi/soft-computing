package replacement;

import core.Population;

public interface ReplacementStrategy {
    Population replace(Population oldPop, Population newPop);
}
