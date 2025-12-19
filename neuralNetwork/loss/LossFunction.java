package neuralNetwork.loss;


public interface LossFunction {
    double calculate(double[] predictions, double[] targets);    
    double[] derivative(double[] predictions, double[] targets);
    String getName();
}
