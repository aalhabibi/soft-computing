package geneticAlgorithm.core;

import geneticAlgorithm.chromosome.Chromosome;

import java.util.Collections;
import java.util.List;

public class Population<T> {
    private List<Chromosome<T>> chromosomeList;

    public Population(List<Chromosome<T>> chromosomeList) {
        this.chromosomeList = chromosomeList;
    }

    public List<Chromosome<T>> getChromosomeList() { return chromosomeList; }
    public Chromosome<T> getBest() { return Collections.max(chromosomeList); }
}
