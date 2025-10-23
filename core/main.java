package core;
import chromosome.IntegerChromosome;
import carParts.CarChromosome;
import core.GeneticAlgorithm;
import core.Population;
import crossover.SinglePoint;
import mutation.IntegerSwapMutation;
import replacement.Elitism;
import selection.RouletteWheel;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {

        // --- Step 1: Create initial population ---
        List<CarChromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CarChromosome c = new CarChromosome(4,0,2);
            c.randomInitialize();
            c.evaluateFitness();
            chromosomes.add(c);
        }

        Population<Integer> initialPopulation = new Population<Integer>(new ArrayList<>(chromosomes));
        System.out.println("Initial population created with " + initialPopulation.getChromosomeList().size() + " chromosomes:");
        System.out.println(initialPopulation.getChromosomeList());


        // --- Step 2: Create GA components ---
        RouletteWheel<Integer> selection = new RouletteWheel<Integer>();
        SinglePoint<Integer> crossover = new SinglePoint<Integer>();
        IntegerSwapMutation mutation = new IntegerSwapMutation();
        Elitism<Integer> replacement = new Elitism<Integer>(2);

        // --- Step 3: Configure GA ---
        GeneticAlgorithm<CarChromosome> ga = new GeneticAlgorithm(initialPopulation, selection, crossover, mutation, replacement);
        ga.setGenerations(5);
        ga.setCrossoverRate(0.8);
        ga.setMutationRate(0.1);

        // --- Step 4: Run GA ---
        ga.run();
    }
}
