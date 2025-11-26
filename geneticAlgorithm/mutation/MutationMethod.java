package geneticAlgorithm.mutation;

import geneticAlgorithm.chromosome.Chromosome;

public interface MutationMethod<T> {
    void mutate(Chromosome<T> chromosome);
}
