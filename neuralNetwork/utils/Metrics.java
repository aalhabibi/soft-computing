package neuralNetwork.utils;

public class Metrics {
    
    public static double accuracy(double[][] predictions, double[][] targets) {
        if (predictions.length != targets.length) {
            throw new IllegalArgumentException("Predictions and targets must have same length");
        }
        
        int correct = 0;
        
        for (int i = 0; i < predictions.length; i++) {
            int predClass = argmax(predictions[i]);
            int trueClass = argmax(targets[i]);
            
            if (predClass == trueClass) {
                correct++;
            }
        }
        
        return (double) correct / predictions.length;
    }
    
    // get index of maximum value in array
    private static int argmax(double[] array) {
        int maxIndex = 0;
        double maxValue = array[0];
        
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        
        return maxIndex;
    }
}
