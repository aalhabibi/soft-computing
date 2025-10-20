package core;

import chromosome.Chromosome;
import crossover.CrossoverMethod;
import mutation.MutationMethod;
import replacement.ReplacementStrategy;
import selection.SelectionMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Set default values for the population size, number of generations, number of
 * parents, selection and crossover methods, crossover and mutation probabilities,
 * etc. However, the GA in your library must allow the user to set/choose these
 * values himself if he wishes to tweak these hyperparameters.
 */
public class GeneticAlgorithm {

    // === GA Components ===
    private SelectionMethod selection;
    private CrossoverMethod crossover;
    private MutationMethod mutation;
    private ReplacementStrategy replacement;
    private Population population;

    // === Hyperparameters (with default values) ===
    private int populationSize = 50;
    private int generations = 100;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.05;
    private int numParents = 2;

    // === Constructors ===
    public GeneticAlgorithm() {
        //empty default constructor — user can set everything manually
    }

    public GeneticAlgorithm(Population initialPopulation,
                            SelectionMethod selection,
                            CrossoverMethod crossover,
                            MutationMethod mutation,
                            ReplacementStrategy replacement) {
        this.population = initialPopulation;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.replacement = replacement;
    }

    // === Setters for customization ===
    public void setPopulation(Population population) { this.population = population; }
    public void setPopulationSize(int populationSize) { this.populationSize = populationSize; }
    public void setGenerations(int generations) { this.generations = generations; }
    public void setCrossoverRate(double crossoverRate) { this.crossoverRate = crossoverRate; }
    public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }
    public void setNumParents(int numParents) { this.numParents = numParents; }

    public void setSelectionMethod(SelectionMethod selection) { this.selection = selection; }
    public void setCrossoverMethod(CrossoverMethod crossover) { this.crossover = crossover; }
    public void setMutationMethod(MutationMethod mutation) { this.mutation = mutation; }
    public void setReplacementStrategy(ReplacementStrategy replacement) { this.replacement = replacement; }

    // === Getters (optional, useful for debugging) ===
    public int getPopulationSize() { return populationSize; }
    public int getGenerations() { return generations; }
    public double getCrossoverRate() { return crossoverRate; }
    public double getMutationRate() { return mutationRate; }

    /**
     * Runs the Genetic Algorithm using the configured parameters and operators.
     */
    public void run() {
        if (population == null || selection == null || crossover == null
                || mutation == null || replacement == null) {
            throw new IllegalStateException("GA components not fully initialized!");
        }

        for (int gen = 0; gen < generations; gen++) {
            List<Chromosome> newGeneration = new ArrayList<>();

            while (newGeneration.size() < population.getChromosomeList().size()) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);

                Chromosome child;

                // Apply crossover with probability
                if (Math.random() < crossoverRate)
                    child = crossover.crossover(parent1, parent2);
                else
                    child = parent1.copy(); // no crossover

                // Apply mutation with probability
                if (Math.random() < mutationRate)
                    mutation.mutate(child);

                child.evaluateFitness();
                newGeneration.add(child);
            }

            Population newPop = new Population(newGeneration);
            population = replacement.replace(population, newPop);

            // Optional: print best fitness every generation
            System.out.println("Generation " + gen + " best: " + population.getBest());
        }

        System.out.println("=== Final Best Individual ===");
        System.out.println(population.getBest());
    }
}
