package neuralNetwork.initialization;

import java.util.Random;


public class XavierInitializer implements WeightInitializer {
    private final Random random;

    public XavierInitializer() {
        this.random = new Random();
    }

    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];
        double stddev = Math.sqrt(2.0 / (inputSize + outputSize));
        
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextGaussian() * stddev;
            }
        }
        
        return weights;
    }
    
    @Override
    public String getName() {
        return "Xavier";
    }
}
