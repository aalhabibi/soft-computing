package crossover;

import chromosome.Chromosome;
import java.util.*;

public class Uniform<T> implements CrossoverMethod<T> {
    private final Random random = new Random();
    private final double swapProbability;

    public Uniform(double swapProbability) {
        this.swapProbability = swapProbability;
    }

    @Override
    public Chromosome<T> crossover(Chromosome<T> parent1, Chromosome<T> parent2) {
        int length = parent1.getGenes().length;
        T[] childGenes = Arrays.copyOf(parent1.getGenes(), length);

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
