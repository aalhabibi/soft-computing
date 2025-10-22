package core;
import chromosome.IntegerChromosome;
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
        List<IntegerChromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            IntegerChromosome c = new IntegerChromosome(5,0,2); // 5 parts in car
            c.randomInitialize();
            c.evaluateFitness();
            chromosomes.add(c);
        }

        Population<Integer> initialPopulation = new Population<Integer>(new ArrayList<>(chromosomes));

        // --- Step 2: Create GA components ---
        RouletteWheel<Integer> selection = new RouletteWheel<Integer>();
        SinglePoint<Integer> crossover = new SinglePoint<Integer>();
        IntegerSwapMutation mutation = new IntegerSwapMutation();
        Elitism<Integer> replacement = new Elitism<Integer>(2);

        // --- Step 3: Configure GA ---
        GeneticAlgorithm ga = new GeneticAlgorithm(initialPopulation, selection, crossover, mutation, replacement);
        ga.setGenerations(50);
        ga.setCrossoverRate(0.8);
        ga.setMutationRate(0.1);

        // --- Step 4: Run GA ---
        ga.run();
    }
}
