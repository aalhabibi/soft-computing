package core;

import java.util.Collections;
import java.util.List;

public class Population {
    private List<Chromosome> chromosomeList;

    public Population(List<Chromosome> chromosomeList) {
        this.chromosomeList = chromosomeList;
    }

    public List<Chromosome> getChromosomeList() { return chromosomeList; }
    public Chromosome getBest() { return Collections.max(chromosomeList); }
}
