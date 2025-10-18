package core;

public abstract class Chromosome implements Comparable<Chromosome> {
    protected double fitness;

    public abstract Chromosome copy();
    public abstract void randomInitialize();
    public abstract void evaluateFitness();
    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    @Override
    public int compareTo(Chromosome other) {
        return Double.compare(other.fitness, this.fitness);
    }
}
