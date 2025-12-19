package neuralNetwork.core;

import neuralNetwork.activation.*;
import neuralNetwork.initialization.*;
import neuralNetwork.loss.*;

public class NetworkBuilder {
    private final NeuralNetwork network;
    private double learningRate = 0.01;
    private LossFunction lossFunction = new MeanSquaredError();
    
    public NetworkBuilder(int inputSize, WeightInitializer initializer) {
        this.network = new NeuralNetwork(inputSize, initializer);
    }

    public NetworkBuilder addLayer(int neurons, ActivationFunction activation) {
        network.addLayer(neurons, activation);
        return this;
    }
    
    public NetworkBuilder addSigmoidLayer(int neurons) {
        return addLayer(neurons, new Sigmoid());
    }
    
    public NetworkBuilder addReLULayer(int neurons) {
        return addLayer(neurons, new ReLU());
    }
    
    public NetworkBuilder addTanhLayer(int neurons) {
        return addLayer(neurons, new Tanh());
    }
    
    public NetworkBuilder addLinearLayer(int neurons) {
        return addLayer(neurons, new Linear());
    }
    
    public NetworkBuilder learningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }
    
    public void lossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
    }
    
    public void mseLoss() {
        lossFunction(new MeanSquaredError());
    }
    
    public void categoricalCrossEntropyLoss() {
        lossFunction(new CategoricalCrossEntropy());
    }
    
    public NeuralNetwork build() {
        network.setLearningRate(learningRate);
        network.setLossFunction(lossFunction);
        return network;
    }
}
