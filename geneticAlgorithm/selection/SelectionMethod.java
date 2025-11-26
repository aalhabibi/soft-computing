package geneticAlgorithm.selection;

import geneticAlgorithm.chromosome.Chromosome;
import geneticAlgorithm.core.Population;

public interface SelectionMethod<T> {
    Chromosome<T> select(Population population);
}
