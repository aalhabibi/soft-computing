package neuralNetwork.activation;

// Linear activation function: f(x) = x
public class Linear implements ActivationFunction {
    
    @Override
    public double activate(double x) {
        return x;
    }
    
    @Override
    public double derivative(double x) {
        return 1.0;
    }
    
    @Override
    public String getName() {
        return "Linear";
    }
}
