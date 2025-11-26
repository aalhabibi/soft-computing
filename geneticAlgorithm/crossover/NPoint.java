package geneticAlgorithm.crossover;

import geneticAlgorithm.chromosome.Chromosome;
import java.util.*;

public class NPoint<T> implements CrossoverMethod<T> {
    private final Random random = new Random();
    private final int nPoints;

    public NPoint(int nPoints) {
        this.nPoints = nPoints;
    }

    @Override
    public Chromosome<T> crossover(Chromosome<T> parent1, Chromosome<T> parent2) {
        int length = parent1.getGenes().length;
        int[] points = random.ints(nPoints, 1, length - 1).sorted().toArray();

        T[] childGenes = Arrays.copyOf(parent1.getGenes(), length);
        boolean fromParent1 = true;
        int nextPointIndex = 0;

        for (int i = 0; i < length; i++) {
            if (nextPointIndex < points.length && i == points[nextPointIndex]) {
                fromParent1 = !fromParent1;
                nextPointIndex++;
            }
            childGenes[i] = fromParent1 ? parent1.getGenes()[i] : parent2.getGenes()[i];
        }

        Chromosome<T> child = parent1.copy();
        child.setGenes(childGenes);
        return child;
    }
}
