package replacement;

import core.Population;
import chromosome.Chromosome;
import java.util.Arrays;

public class SteadyState implements ReplacementStrategy {
    private int k; // number of individuals to replace

    public SteadyState(int k) {
        this.k = k;
    }

    @Override
    public Population replace(Population oldPop, Population newPop) {
        Chromosome[] oldArray = oldPop.getChromosomeList().toArray(new Chromosome[0]);
        Chromosome[] newArray = newPop.getChromosomeList().toArray(new Chromosome[0]);

        // Sort both arrays by fitness descending
        Arrays.sort(oldArray);
        Arrays.sort(newArray);

        // Replace the k worst individuals from old population with the k best new ones
        for (int i = 0; i < k && i < oldArray.length && i < newArray.length; i++) {
            oldArray[oldArray.length - 1 - i] = newArray[i];
        }

        return new Population(Arrays.asList(oldArray));
    }
}
