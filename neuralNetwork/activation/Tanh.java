package neuralNetwork.activation;


// Tanh activation function: f(x) = tanh(x)
public class Tanh implements ActivationFunction {
    
    @Override
    public double activate(double x) {
        return Math.tanh(x);
    }
    
    @Override
    public double derivative(double x) {
        double tanh = Math.tanh(x);
        return 1.0 - tanh * tanh;
    }
    
    @Override
    public String getName() {
        return "Tanh";
    }
}
