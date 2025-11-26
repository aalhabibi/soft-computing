package geneticAlgorithm.crossover;

import geneticAlgorithm.chromosome.Chromosome;

public interface CrossoverMethod<T> {
    Chromosome<T> crossover(Chromosome<T> parent1, Chromosome<T> parent2);
}
