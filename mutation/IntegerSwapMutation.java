package mutation;

import chromosome.Chromosome;
import chromosome.IntegerChromosome;
import java.util.Random;

//For integer chromosomes ONLY
public class IntegerSwapMutation implements MutationMethod {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome chromosome) {
        IntegerChromosome intChromosome = (IntegerChromosome) chromosome;
        int[] genes = intChromosome.getGenes();

        int i = random.nextInt(genes.length);
        int j = random.nextInt(genes.length);

        int temp = genes[i];
        genes[i] = genes[j];
        genes[j] = temp;

        intChromosome.setGenes(genes);
    }
}
