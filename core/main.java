package core;


import carParts.CarChromosome;
import crossover.*;
import mutation.IntegerSwapMutation;
import replacement.Elitism;
import selection.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Genetic Algorithm Configuration ===");


        System.out.print("Enter population size (default 30): ");
        int populationSize = safeIntInput(sc, 30);

        System.out.print("Enter number of generations (default 5): ");
        int generations = safeIntInput(sc, 5);

        System.out.print("Enter crossover rate [0-1] (default 0.8): ");
        double crossoverRate = safeDoubleInput(sc, 0.8);

        System.out.print("Enter mutation rate [0-1] (default 0.1): ");
        double mutationRate = safeDoubleInput(sc, 0.1);

        System.out.println("\nSelect Selection Method:");
        System.out.println("1. Roulette Wheel (default)");
        System.out.println("2. Tournament Selection");
        System.out.println("3. Rank Selection");
        int selectionChoice = safeIntInput(sc, 1);

        System.out.println("\nSelect Crossover Method:");
        System.out.println("1. Single Point (default)");
        System.out.println("2. Two Point");
        System.out.println("3. Uniform Crossover");
        int crossoverChoice = safeIntInput(sc, 1);

        System.out.println("\nUsing default mutation and replacement methods:");
        System.out.println("- Mutation: IntegerSwapMutation");
        System.out.println("- Replacement: Elitism (k=2)");


        List<CarChromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            CarChromosome c = new CarChromosome(4, 0, 2);
            c.randomInitialize();
            c.evaluateFitness();
            chromosomes.add(c);
        }

        Population<Integer> initialPopulation = new Population<Integer>(new ArrayList<>(chromosomes));
        System.out.println("\nInitial population created with " + populationSize + " chromosomes.");
        System.out.println(initialPopulation.getChromosomeList());


        SelectionMethod<CarChromosome> selection;
        switch (selectionChoice) {
            case 2 -> selection = new Tournament<>(2);
            case 3 -> selection = new RouletteWheel<>();
            default -> selection = new RouletteWheel<>();
        }

        CrossoverMethod<CarChromosome> crossover;
        switch (crossoverChoice) {
            case 2 -> crossover = new NPoint<>(2);
            case 3 -> crossover = new Uniform<>(0.5);
            default -> crossover = new SinglePoint<>();
        }

        IntegerSwapMutation mutation = new IntegerSwapMutation();
        Elitism<CarChromosome> replacement = new Elitism<>(2);


        GeneticAlgorithm<CarChromosome> ga = new GeneticAlgorithm<>(initialPopulation, selection, crossover, mutation, replacement);
        ga.setGenerations(generations);
        ga.setCrossoverRate(crossoverRate);
        ga.setMutationRate(mutationRate);
        ga.setPopulationSize(populationSize);


        ga.run();

        sc.close();
    }

    // helpers
    private static int safeIntInput(Scanner sc, int defaultVal) {
        String input = sc.nextLine().trim();
        try {
            return input.isEmpty() ? defaultVal : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using default: " + defaultVal);
            return defaultVal;
        }
    }

    private static double safeDoubleInput(Scanner sc, double defaultVal) {
        String input = sc.nextLine().trim();
        try {
            double val = input.isEmpty() ? defaultVal : Double.parseDouble(input);
            if (val < 0 || val > 1) throw new NumberFormatException();
            return val;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using default: " + defaultVal);
            return defaultVal;
        }
    }
}
