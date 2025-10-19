package crossover;

import chromosome.Chromosome;

public interface CrossoverMethod {
    Chromosome crossover(Chromosome parent1, Chromosome parent2);
}
