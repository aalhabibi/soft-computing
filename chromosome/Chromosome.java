package chromosome;

public abstract class Chromosome<T> implements Comparable<Chromosome<T>> {
    public double fitness;

    public abstract Chromosome<T> copy();
    public abstract void randomInitialize();
    public abstract double evaluateFitness();
    public abstract T[] getGenes();
    public abstract void setGenes(T[] genes);
    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    @Override
    public int compareTo(Chromosome<T> other) {
        return Double.compare(this.fitness, other.fitness);
    }
}
