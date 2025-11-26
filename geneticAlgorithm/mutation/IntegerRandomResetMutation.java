package geneticAlgorithm.mutation;

import geneticAlgorithm.chromosome.Chromosome;
import geneticAlgorithm.chromosome.IntegerChromosome;
import java.util.Random;

//For integer chromosomes ONLY
public class IntegerRandomResetMutation implements MutationMethod<Integer> {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome<Integer> chromosome) {
        IntegerChromosome intChromosome = (IntegerChromosome) chromosome;
        Integer[] genes = intChromosome.getGenes();
        int index = random.nextInt(genes.length);
        int newValue = intChromosome.randomValueForGene(index);
        genes[index] = newValue;
        intChromosome.setGenes(genes);
    }
}
