package mutation;

import chromosome.Chromosome;
import chromosome.FloatingPointChromosome;
import java.util.Random;

//For floating chromosomes ONLY
public class UniformFloatingPointMutation implements MutationMethod<Double> {
    private final Random random = new Random();

    @Override
    public void mutate(Chromosome<Double> chromosome) {
        FloatingPointChromosome fpChromosome = (FloatingPointChromosome) chromosome;
        Double[] genes = fpChromosome.getGenes();

        double min = getMinValue(fpChromosome);
        double max = getMaxValue(fpChromosome);

        for (int i = 0; i < genes.length; i++) {
            double xi = genes[i];
            double deltaL = xi - min;
            double deltaU = max - xi;

            double ri1 = random.nextDouble(); // [0, 1]
            double delta;
            double ri2;

            if (ri1 <= 0.5) {
                // move left (decrease)
                ri2 = random.nextDouble() * deltaL;
                delta = -ri2;
            } else {
                // move right (increase)
                ri2 = random.nextDouble() * deltaU;
                delta = ri2;
            }

            double xiNew = xi + delta;

            // Clamp within bounds
            xiNew = Math.max(min, Math.min(max, xiNew));

            genes[i] = xiNew;
        }

        fpChromosome.setGenes(genes);
    }

    private double getMinValue(FloatingPointChromosome chromosome) {
        try {
            var field = FloatingPointChromosome.class.getDeclaredField("minValue");
            field.setAccessible(true);
            return field.getDouble(chromosome);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access minValue", e);
        }
    }

    private double getMaxValue(FloatingPointChromosome chromosome) {
        try {
            var field = FloatingPointChromosome.class.getDeclaredField("maxValue");
            field.setAccessible(true);
            return field.getDouble(chromosome);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access maxValue", e);
        }
    }
}
