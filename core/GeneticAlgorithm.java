package core;

import crossover.CrossoverMethod;
import mutation.MutationMethod;
import replacement.ReplacementStrategy;
import selection.SelectionMethod;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    private SelectionMethod selection;
    private CrossoverMethod crossover;
    private MutationMethod mutation;
    private ReplacementStrategy replacement;
    private Population population;

    public void run(int generations) {
        for (int i = 0; i < generations; i++) {
            List<Chromosome> newGeneration = new ArrayList<Chromosome>();

            while (newGeneration.size() < population.getChromosomeList().size()) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                child.evaluateFitness();
                newGeneration.add(child);
            }

            Population newPop = new Population(newGeneration);
            population = replacement.replace(population, newPop);
        }
    }
}
