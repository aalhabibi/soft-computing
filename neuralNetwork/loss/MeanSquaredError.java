package neuralNetwork.loss;


public class MeanSquaredError implements LossFunction {
    
    @Override
    public double calculate(double[] predictions, double[] targets) {
        if (predictions.length != targets.length) {
            throw new IllegalArgumentException("Predictions and targets must have same length");
        }
        
        double sum = 0.0;
        for (int i = 0; i < predictions.length; i++) {
            double diff = predictions[i] - targets[i];
            sum += diff * diff;
        }
        
        return sum / predictions.length;
    }
    
    @Override
    public double[] derivative(double[] predictions, double[] targets) {
        if (predictions.length != targets.length) {
            throw new IllegalArgumentException("Predictions and targets must have same length");
        }
        
        double[] gradient = new double[predictions.length];
        for (int i = 0; i < predictions.length; i++) {
            gradient[i] = 2.0 * (predictions[i] - targets[i]) / predictions.length;
        }
        
        return gradient;
    }
    
    @Override
    public String getName() {
        return "MeanSquaredError";
    }
}
