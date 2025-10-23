package core;

import chromosome.Chromosome;
import crossover.CrossoverMethod;
import mutation.MutationMethod;
import replacement.ReplacementStrategy;
import selection.SelectionMethod;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm<T> {

    private SelectionMethod selection;
    private CrossoverMethod crossover;
    private MutationMethod mutation;
    private ReplacementStrategy replacement;
    private Population population;

    private int populationSize = 50;
    private int generations = 100;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.05;
    private int numParents = 2;

    public GeneticAlgorithm() {}

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

    public void run() {
        if (population == null || selection == null || crossover == null
                || mutation == null || replacement == null) {
            throw new IllegalStateException("GA components not fully initialized!");
        }

        System.out.println("\n=== Starting Genetic Algorithm ===");
        System.out.println("Population size: " + populationSize);
        System.out.println("Generations: " + generations);
        System.out.println("Crossover rate: " + crossoverRate);
        System.out.println("Mutation rate: " + mutationRate);
        System.out.println("------------------------------------");

        for (int gen = 0; gen < generations; gen++) {
            System.out.println("\n>>> Generation " + gen + " <<<");

            for (Object obj : population.getChromosomeList()) {
                Chromosome<T> c = (Chromosome<T>) obj;

                c.evaluateFitness();
            }


            List<Chromosome<T>> newGeneration = new ArrayList<>();

            while (newGeneration.size() < population.getChromosomeList().size()) {
                Chromosome<T> parent1 = selection.select(population);
                Chromosome<T> parent2 = selection.select(population);

                System.out.println("\nSelected Parents:");
                System.out.println("Parent 1: " + parent1);
                System.out.println("Parent 2: " + parent2);

                Chromosome<T> child;

                if (Math.random() < crossoverRate) {
                    System.out.println("Performing crossover...");
                    child = crossover.crossover(parent1, parent2);
                } else {
                    System.out.println("Skipping crossover (copying parent1)...");
                    child = parent1.copy();
                }
                child.evaluateFitness();
                System.out.println("Child after crossover : " + child);

                if (Math.random() < mutationRate) {
                    System.out.println("Applying mutation...");
                    mutation.mutate(child);
                } else {
                    System.out.println("Skipping mutation...");
                }
                child.evaluateFitness();
                System.out.println("Child after mutation: " + child);


                System.out.println("Child fitness: " + child.evaluateFitness());

                newGeneration.add(child);
            }

            Population newPop = new Population(newGeneration);
            System.out.println("\nReplacing old population...");

            population = replacement.replace(population, newPop);


            for (Object obj : population.getChromosomeList()) {
                Chromosome<T> c = (Chromosome<T>) obj;
                c.evaluateFitness();
            }
            System.out.println("\nNew population: " + population.getChromosomeList());


            Chromosome<T> best = population.getBest();
            System.out.println("Best individual this generation: " + best);
        }

        System.out.println("\n=== Final Best Individual ===");
        System.out.println(population.getBest());
    }
}
