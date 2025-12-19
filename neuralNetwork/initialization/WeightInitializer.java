package neuralNetwork.initialization;


public interface WeightInitializer {
    double[][] initialize(int inputSize, int outputSize);
    String getName();
}
