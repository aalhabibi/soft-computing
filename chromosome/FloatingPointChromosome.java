package chromosome;

import java.util.Arrays;
import java.util.Random;

public class FloatingPointChromosome extends Chromosome {
    private double[] genes;
    private double minValue;
    private double maxValue;
    private Random rand=new Random();

    public FloatingPointChromosome(int length, double minValue, double maxValue) {
        this.genes = new double[length];
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void randomInitialize() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = minValue + (maxValue - minValue) * rand.nextDouble();
        }
    }

    @Override
    public double evaluateFitness() {
        // Example: minimize distance to target vector (e.g., all 0s)
        double sum = 0;
        for (double g : genes) sum += Math.pow(g, 2);
        fitness = -sum; // smaller magnitude = better fitness
    }

    @Override
    public Chromosome copy() {
        FloatingPointChromosome clone = new FloatingPointChromosome(genes.length, minValue, maxValue);
        clone.genes = Arrays.copyOf(this.genes, this.genes.length);
        clone.fitness = this.fitness;
        return clone;
    }

    public double[] getGenes() { return genes; }

    @Override
    public String toString() {
        return Arrays.toString(genes) + " (fitness=" + fitness + ")";
    }
}
