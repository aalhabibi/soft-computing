package neuralNetwork.core;

import neuralNetwork.activation.ActivationFunction;
import neuralNetwork.initialization.WeightInitializer;
import neuralNetwork.loss.LossFunction;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private final List<Layer> layers;
    private LossFunction lossFunction;
    private double learningRate;
    private final int inputSize;
    private final WeightInitializer weightInitializer;
    
    public NeuralNetwork(int inputSize, WeightInitializer weightInitializer) {
        this.inputSize = inputSize;
        this.layers = new ArrayList<>();
        this.weightInitializer = weightInitializer;
        this.learningRate = 0.01; // default
    }
    
    public void addLayer(int neurons, ActivationFunction activation) {
        int previousSize = layers.isEmpty() ? inputSize : layers.getLast().getNeurons();
        Layer layer = new Layer(previousSize, neurons, activation, weightInitializer);
        layers.add(layer);
    }
    
    public void setLossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
    }
    
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
    
    // forward propagation
    public double[] forward(double[] input) {
        if (input.length != inputSize) {
            throw new IllegalArgumentException("Input size mismatch. Expected: " + inputSize + ", got: " + input.length);
        }
        
        double[] output = input;
        for (Layer layer : layers) {
            output = layer.forward(output);
        }
        return output;
    }
    
    // backward propagation
    public void backward(double[] target) {
        if (lossFunction == null) {
            throw new IllegalStateException("Loss function not set");
        }
        
        double[] output = layers.getLast().getOutput();
        
        double[] gradient = lossFunction.derivative(output, target);
        
        for (int i = layers.size() - 1; i >= 0; i--) {
            gradient = layers.get(i).backward(gradient);
        }
    }
    
    private void updateWeights(int batchSize) {
        for (Layer layer : layers) {
            layer.updateWeights(learningRate, batchSize);
        }
    }
    
    public double trainBatch(double[][] inputs, double[][] targets) {
        double totalLoss = 0.0;
        
        // across all samples in batch
        for (int i = 0; i < inputs.length; i++) {

            double[] output = forward(inputs[i]);
            
            totalLoss += lossFunction.calculate(output, targets[i]);
            
            backward(targets[i]);
        }

        // update once per batch (stochastic GD)
        updateWeights(inputs.length);
        
        return totalLoss / inputs.length;
    }
    
    public double[] predict(double[] input) {
        return forward(input);
    }
    
    public double[][] predict(double[][] inputs) {
        double[][] predictions = new double[inputs.length][];
        for (int i = 0; i < inputs.length; i++) {
            predictions[i] = predict(inputs[i]);
        }
        return predictions;
    }
}
