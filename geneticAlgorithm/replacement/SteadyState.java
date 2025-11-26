package geneticAlgorithm.replacement;

import geneticAlgorithm.core.Population;
import geneticAlgorithm.chromosome.Chromosome;
import java.util.Arrays;

public class SteadyState<T> implements ReplacementStrategy<T> {
    private int k; // number of individuals to replace

    public SteadyState(int k) {
        this.k = k;
    }

    @Override
    public Population<T> replace(Population<T> oldPop, Population<T> newPop) {
        Chromosome<T>[] oldArray = oldPop.getChromosomeList().toArray(new Chromosome[0]);
        Chromosome<T>[] newArray = newPop.getChromosomeList().toArray(new Chromosome[0]);

        // Sort both arrays by fitness descending
        Arrays.sort(oldArray);
        Arrays.sort(newArray);

        // Replace the k worst individuals from old population with the k best new ones
        for (int i = 0; i < k && i < oldArray.length && i < newArray.length; i++) {
            oldArray[oldArray.length - 1 - i] = newArray[i];
        }

        return new Population<T>(Arrays.asList(oldArray));
    }
}
