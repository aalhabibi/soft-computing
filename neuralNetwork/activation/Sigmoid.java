package neuralNetwork.activation;


// Sigmoid activation function: f(x) = 1 / (1 + e^(-x))
public class Sigmoid implements ActivationFunction {
    
    @Override
    public double activate(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    @Override
    public double derivative(double x) {
        double sigmoid = activate(x);
        return sigmoid * (1.0 - sigmoid);
    }
    
    @Override
    public String getName() {
        return "Sigmoid";
    }
}
