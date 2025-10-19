package mutation;

import chromosome.BinaryChromosome;
import chromosome.Chromosome;

import java.util.Random;


//For binary chromosomes ONLY
public class BitFlipMutation implements MutationMethod {
    private final double mutationRate;
    private final Random random = new Random();

    public BitFlipMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public void mutate(Chromosome chromosome) {
        BinaryChromosome binary = (BinaryChromosome) chromosome;
        int[] genes = binary.getGenes();

        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                if (genes[i] == 0) {genes[i] = 1;}
                else if (genes[i] == 1) {genes[i] = 0;}
            }
        }
        binary.setGenes(genes);
    }
}
