package crossover;

import chromosome.Chromosome;
import java.util.*;

public class SinglePoint<T> implements CrossoverMethod<T> {
    private final Random random = new Random();

    @Override
    public Chromosome<T> crossover(Chromosome<T> parent1, Chromosome<T> parent2) {
        int length = parent1.getGenes().length;
        int point = random.nextInt(length); // crossover point

        T[] childGenes = Arrays.copyOf(parent1.getGenes(), length);
        for (int i = 0; i < length; i++) {
            childGenes[i] = (i < point) ? parent1.getGenes()[i] : parent2.getGenes()[i];
        }

        Chromosome child = parent1.copy();
        child.setGenes(childGenes);
        return child;
    }
}
