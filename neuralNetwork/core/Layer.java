package neuralNetwork.core;

import neuralNetwork.activation.ActivationFunction;
import neuralNetwork.initialization.WeightInitializer;

public class Layer {
    private final double[][] weights;
    private final double[] biases;
    private final ActivationFunction activation;
    
    private double[] input;
    private double[] weightedSum;
    private double[] output;
    
    private final double[][] weightGradients;
    private final double[] biasGradients;
    
    private final int inputSize;
    private final int neurons;
    
    public Layer(int inputSize, int neurons, ActivationFunction activation, WeightInitializer initializer) {
        this.inputSize = inputSize;
        this.neurons = neurons;
        this.activation = activation;
        
        this.weights = initializer.initialize(inputSize, neurons);
        this.biases = new double[neurons];
        this.weightGradients = new double[inputSize][neurons];
        this.biasGradients = new double[neurons];
    }
    
    public double[] forward(double[] input) {
        if (input.length != inputSize) {
            throw new IllegalArgumentException("Input size mismatch");
        }
        
        this.input = input.clone();
        this.weightedSum = new double[neurons];
        this.output = new double[neurons];
        
        // calculate weighted sum = activation(weights * input + bias)
        for (int i = 0; i < neurons; i++) {
            double sum = biases[i];
            for (int j = 0; j < inputSize; j++) {
                sum += weights[j][i] * input[j];
            }
            weightedSum[i] = sum;
            output[i] = activation.activate(sum);
        }
        
        return output.clone();
    }
    
    public double[] backward(double[] outputGradient) {
        double[] delta = new double[neurons];
        for (int i = 0; i < neurons; i++) {
            delta[i] = outputGradient[i] * activation.derivative(weightedSum[i]);
        }
        
        // accumulate gradients for weights and biases
        for (int j = 0; j < inputSize; j++) {
            for (int i = 0; i < neurons; i++) {
                weightGradients[j][i] += delta[i] * input[j];
            }
        }
        
        for (int i = 0; i < neurons; i++) {
            biasGradients[i] += delta[i];
        }
        
        // calculate gradient for previous layer
        double[] inputGradient = new double[inputSize];
        for (int j = 0; j < inputSize; j++) {
            double sum = 0.0;
            for (int i = 0; i < neurons; i++) {
                sum += delta[i] * weights[j][i];
            }
            inputGradient[j] = sum;
        }
        
        return inputGradient;
    }
    
    // update weights using accumulated gradients
    public void updateWeights(double learningRate, int batchSize) {
        for (int j = 0; j < inputSize; j++) {
            for (int i = 0; i < neurons; i++) {
                weights[j][i] -= learningRate * weightGradients[j][i] / batchSize;
                weightGradients[j][i] = 0; // Reset
            }
        }
        
        for (int i = 0; i < neurons; i++) {
            biases[i] -= learningRate * biasGradients[i] / batchSize;
            biasGradients[i] = 0; // Reset
        }
    }
    
    public int getNeurons() { return neurons; }
    public double[] getOutput() { return output != null ? output.clone() : null; }
}
