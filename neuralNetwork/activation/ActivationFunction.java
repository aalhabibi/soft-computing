package neuralNetwork.activation;


public interface ActivationFunction {
    double activate(double x);
    double derivative(double x);
    String getName();
}
