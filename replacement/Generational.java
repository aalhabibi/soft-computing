package replacement;

import core.Population;

public class Generational implements ReplacementStrategy {
    @Override
    public Population replace(Population oldPop, Population newPop) {
        // Full replacement — new population completely replaces old one
        return newPop;
    }
}
