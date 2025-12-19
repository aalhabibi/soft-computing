package neuralNetwork.initialization;

import java.util.Random;

public class RandomUniform implements WeightInitializer {
    private final Random random;
    private final double bound;

    public RandomUniform() {
        this(0.5);
    }

    public RandomUniform(double bound) {
        this.random = new Random();
        this.bound = bound;
    }
    
    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];
        
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextDouble() * 2 * bound - bound;
            }
        }
        
        return weights;
    }
    
    @Override
    public String getName() {
        return "RandomUniform(bound=" + bound + ")";
    }
}
