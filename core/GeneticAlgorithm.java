package core;

import chromosome.*;

import crossover.*;
import mutation.*;

import replacement.*;


import selection.*;

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


    public GeneticAlgorithm() {
        // Set default methods and parameters
        this.selection = new RouletteWheel();
        this.crossover = new SinglePoint();
        this.mutation = new IntegerSwapMutation();
        this.replacement = new Elitism(2);
        this.population = null; // must be set later by user
    }

    public GeneticAlgorithm(Population initialPopulation,
                            SelectionMethod selection,
                            CrossoverMethod crossover,
                            MutationMethod mutation,
                            ReplacementStrategy replacement) {
        this.population = initialPopulation;
        this.selection = selection != null ? selection : new RouletteWheel();
        this.crossover = crossover != null ? crossover : new SinglePoint();
        this.mutation = mutation != null ? mutation : new IntegerSwapMutation();
        this.replacement = replacement != null ? replacement : new Elitism(2);
    }


    public void setPopulation(Population population) { this.population = population; }
    public void setPopulationSize(int populationSize) { this.populationSize = populationSize; }
    public void setGenerations(int generations) { this.generations = generations; }
    public void setCrossoverRate(double crossoverRate) { this.crossoverRate = crossoverRate; }
    public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }


    public void setSelectionMethod(SelectionMethod selection) { this.selection = selection; }
    public void setCrossoverMethod(CrossoverMethod crossover) { this.crossover = crossover; }
    public void setMutationMethod(MutationMethod mutation) { this.mutation = mutation; }
    public void setReplacementStrategy(ReplacementStrategy replacement) { this.replacement = replacement; }

    public void run() {
        if (population == null) {
            throw new IllegalStateException("Population not initialized!");
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

                Chromosome<T> child1, child2;
                if (Math.random() < crossoverRate) {
                    System.out.println("Performing crossover...");
                    child1 = crossover.crossover(parent1, parent2);
                    child2 = crossover.crossover(parent2, parent1);
                } else {
                    System.out.println("Skipping crossover (copying parent1)...");
                    child1 = parent1.copy();
                    child2 = parent2.copy();
                }

                child1.evaluateFitness();
                child2.evaluateFitness();
                System.out.println("Child 1 after crossover: " + child1);
                System.out.println("Child 2 after crossover: " + child2);

                if (Math.random() < mutationRate){
                    System.out.println("Applying mutation on Child 1...");
                    mutation.mutate(child1);
                }

                else {
                    System.out.println("Skipping mutation for Child 1...");
                }
                child1.evaluateFitness();
                System.out.println("Child 1 after mutation: " + child1);
                if (Math.random() < mutationRate){
                    System.out.println("Applying mutation on Child 2...");
                    mutation.mutate(child2);
                }
                else {
                    System.out.println("Skipping mutation for Child 2...");
                }
                child2.evaluateFitness();
                System.out.println("Child 2 after mutation: " + child2);


                newGeneration.add(child1);
                if (newGeneration.size() < population.getChromosomeList().size())
                    newGeneration.add(child2);
            }

            Population newPop = new Population(newGeneration);

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
