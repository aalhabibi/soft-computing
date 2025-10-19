package crossover;

import chromosome.Chromosome;
import java.util.*;

public class Uniform implements CrossoverMethod {
    private final Random random = new Random();
    private final double swapProbability;

    public Uniform(double swapProbability) {
        this.swapProbability = swapProbability;
    }

    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int length = parent1.getGenes().length;
        Object[] childGenes = new Object[length];

        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < swapProbability)
                childGenes[i] = parent2.getGenes()[i];
            else
                childGenes[i] = parent1.getGenes()[i];
        }

        Chromosome child = parent1.copy();
        child.setGenes(childGenes);
        return child;
    }
}
