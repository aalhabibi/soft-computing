package replacement;

import core.Population;
import chromosome.Chromosome;
import java.util.Arrays;

public class Elitism implements ReplacementStrategy {
    private int eliteCount;

    public Elitism(int eliteCount) {
        this.eliteCount = eliteCount;
    }

    @Override
    public Population replace(Population oldPop, Population newPop) {
        Chromosome[] oldArray = oldPop.getChromosomeList().toArray(new Chromosome[0]);
        Chromosome[] newArray = newPop.getChromosomeList().toArray(new Chromosome[0]);

        //sort populations by fitness
        Arrays.sort(oldArray);
        Arrays.sort(newArray);

        //replace worst with best
        for (int i = 0; i < eliteCount && i < oldArray.length; i++) {
            newArray[newArray.length - 1 - i] = oldArray[i];
        }

        return new Population(Arrays.asList(newArray));
    }
}
