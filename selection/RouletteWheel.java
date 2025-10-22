package selection;

import chromosome.Chromosome;
import core.*;
import java.util.List;
import java.util.Random;

public class RouletteWheel<T> implements SelectionMethod<T> {
    private final Random random = new Random();

    @Override
    public Chromosome<T> select(Population population) {
        List<Chromosome<T>> chromosomes = population.getChromosomeList();
        double totalFitness = chromosomes.stream().mapToDouble(Chromosome::getFitness).sum();
        double point = random.nextDouble() * totalFitness;

        double runningSum = 0;
        for (Chromosome<T> c : chromosomes) {
            runningSum += c.getFitness();
            if (runningSum >= point)
                return c;
        }
        return chromosomes.getLast(); // fallback
    }
}
