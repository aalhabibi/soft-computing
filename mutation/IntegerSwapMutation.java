package mutation;

import chromosome.Chromosome;
import chromosome.IntegerChromosome;
import java.util.Random;

//For integer chromosomes ONLY
public class IntegerSwapMutation implements MutationMethod<Integer> {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome<Integer> chromosome) {
        IntegerChromosome intChromosome = (IntegerChromosome) chromosome;
        Object[] genes = intChromosome.getGenes();

        int i = random.nextInt(genes.length);
        int j = random.nextInt(genes.length);

        int temp = (int) genes[i];
        genes[i] = genes[j];
        genes[j] = temp;

        intChromosome.setGenes((Integer[]) genes);
    }
}
