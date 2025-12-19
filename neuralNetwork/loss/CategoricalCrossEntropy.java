package neuralNetwork.loss;


public class CategoricalCrossEntropy implements LossFunction {
    private static final double EPSILON = 1e-15; // to avoid log(0)
    
    @Override
    public double calculate(double[] predictions, double[] targets) {
        if (predictions.length != targets.length) {
            throw new IllegalArgumentException("Predictions and targets must have same length");
        }
        
        double sum = 0.0;
        for (int i = 0; i < predictions.length; i++) {
            double pred = Math.max(EPSILON, Math.min(1.0 - EPSILON, predictions[i]));
            sum += targets[i] * Math.log(pred);
        }
        
        return -sum;
    }
    
    @Override
    public double[] derivative(double[] predictions, double[] targets) {
        if (predictions.length != targets.length) {
            throw new IllegalArgumentException("Predictions and targets must have same length");
        }
        
        double[] gradient = new double[predictions.length];
        for (int i = 0; i < predictions.length; i++) {
            double pred = Math.max(EPSILON, Math.min(1.0 - EPSILON, predictions[i]));
            gradient[i] = -targets[i] / pred;
        }
        
        return gradient;
    }
    
    @Override
    public String getName() {
        return "CategoricalCrossEntropy";
    }
}
