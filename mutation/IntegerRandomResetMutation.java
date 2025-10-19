package mutation;

import chromosome.Chromosome;
import chromosome.IntegerChromosome;
import java.util.Random;

//For integer chromosomes ONLY
public class IntegerRandomResetMutation implements MutationMethod {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {
        IntegerChromosome intChromosome = (IntegerChromosome) chromosome;
        int[] genes = intChromosome.getGenes();
        int index = random.nextInt(genes.length);
        int newValue = intChromosome.randomValueForGene(index);
        genes[index] = newValue;
        intChromosome.setGenes(genes);
    }
}
