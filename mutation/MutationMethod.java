package mutation;

import chromosome.Chromosome;

public interface MutationMethod<T> {
    void mutate(Chromosome<T> chromosome);
}
