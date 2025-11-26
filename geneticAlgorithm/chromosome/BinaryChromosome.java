package geneticAlgorithm.chromosome;

import java.util.Arrays;
import java.util.Random;

public abstract class BinaryChromosome extends Chromosome<Integer> {
    private Integer[] genes;
    private Random rand = new Random();

    public BinaryChromosome(int length) {
        this.genes = new Integer[length];
    }

    @Override
    public void randomInitialize() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = rand.nextBoolean() ? 1 : 0;
        }
    }

    @Override
    public double evaluateFitness() {
        // Example fitness: maximize number of 1s
        int sum = 0;
        for (Integer g : genes) sum += (Integer) g;
        fitness = sum;
        return fitness;
    }

    @Override
    public Chromosome<Integer> copy() {
        try {
            BinaryChromosome clone = this.getClass()
                    .getDeclaredConstructor(int.class, int.class, int.class)
                    .newInstance(getGenes().length, 0, 1);
            clone.setGenes(Arrays.copyOf(this.getGenes(), this.getGenes().length));
            clone.setFitness(this.getFitness());
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy chromosome", e);
        }
    }

    @Override
    public Integer[] getGenes() { return genes; }

    @Override
    public void setGenes(Integer[] genes) {this.genes = genes;}

    @Override
    public String toString() {
        return Arrays.toString(genes) + " (fitness=" + fitness + ")";
    }
}
