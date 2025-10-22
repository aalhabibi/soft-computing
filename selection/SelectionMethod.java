package selection;

import chromosome.Chromosome;
import core.Population;

public interface SelectionMethod<T> {
    Chromosome<T> select(Population population);
}
