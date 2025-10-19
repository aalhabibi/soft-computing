package selection;

import chromosome.Chromosome;
import core.Population;

public interface SelectionMethod {
    Chromosome select(Population population);
}
