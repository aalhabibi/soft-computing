package chromosome;

import java.util.Arrays;
import java.util.Random;

public class IntegerChromosome extends Chromosome {
    private int[] genes;
    private int minValue;
    private int maxValue;
    private Random rand=new Random();

    public IntegerChromosome(int length, int minValue, int maxValue) {
        this.genes = new int[length];
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void randomInitialize() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = rand.nextInt(maxValue - minValue + 1) + minValue;
        }
    }

    @Override
    public double evaluateFitness() {
        // Example: sum of values (to maximize)
        int sum = 0;
        for (int g : genes) sum += g;
        fitness = sum;
        return fitness;
    }

    @Override
    public Chromosome copy() {
        IntegerChromosome clone = new IntegerChromosome(genes.length, minValue, maxValue);
        clone.genes = Arrays.copyOf(this.genes, this.genes.length);
        clone.fitness = this.fitness;
        return clone;
    }

    public int[] getGenes() { return genes; }

    @Override
    public String toString() {
        return Arrays.toString(genes) + " (fitness=" + fitness + ")";
    }
}
