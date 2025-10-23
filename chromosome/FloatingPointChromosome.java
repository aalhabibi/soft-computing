package chromosome;

import java.util.Arrays;
import java.util.Random;

public abstract class FloatingPointChromosome extends Chromosome<Double> {
    private Double[] genes;
    private double minValue;
    private double maxValue;
    private Random rand=new Random();

    public FloatingPointChromosome(int length, double minValue, double maxValue) {
        this.genes = new Double[length];
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

        double sum = 0;
        for (Double g : genes) sum += Math.pow((Double) g, 2);
        fitness = sum;
        return fitness;
    }

    @Override
    public Chromosome<Double> copy() {
        try {
            FloatingPointChromosome clone = this.getClass()
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
    public Double[] getGenes() { return genes; }

    @Override
    public void setGenes(Double[] genes) {this.genes = genes;}

    @Override
    public String toString() {
        return Arrays.toString(genes) + " (fitness=" + fitness + ")";
    }
}
