package chromosome;

import java.util.Arrays;
import java.util.Random;

public abstract class IntegerChromosome extends Chromosome<Integer> {
    private Integer[] genes;
    private int minValue;
    private int maxValue;
    private Random rand=new Random();

    public IntegerChromosome(int length, int minValue, int maxValue) {
        this.genes = new Integer[length];
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
        for (Integer g : genes) sum += (Integer) g;
        fitness = sum;
        return fitness;
    }

    @Override
    public Chromosome<Integer> copy() {
        try {
            IntegerChromosome clone = this.getClass()
                    .getDeclaredConstructor(int.class, int.class, int.class)
                    .newInstance(getGenes().length, minValue, maxValue);
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

    public int randomValueForGene(int index) {
        return rand.nextInt(maxValue - minValue + 1) + minValue;
    }
}
