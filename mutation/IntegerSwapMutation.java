package mutation;

import chromosome.Chromosome;
import chromosome.IntegerChromosome;
import java.util.Random;

// For integer chromosomes ONLY
public class IntegerSwapMutation implements MutationMethod<Integer> {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome<Integer> chromosome) {
        IntegerChromosome intChromosome = (IntegerChromosome) chromosome;
        Integer[] genes = intChromosome.getGenes(); // Use Integer[] directly

        int length = genes.length;
        int i = random.nextInt(length);
        int j;
        do {
            j = random.nextInt(length);
        } while (j == i); // ensure i != j to actually swap different genes

        int temp = genes[i];
        genes[i] = genes[j];
        genes[j] = temp;

        intChromosome.setGenes(genes);
    }
}