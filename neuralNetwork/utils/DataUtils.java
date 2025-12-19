package neuralNetwork.utils;

import java.util.Random;

public class DataUtils {
    
    public static DataSplit trainTestSplit(double[][] X, double[][] y, double testRatio, long seed) {
        validateData(X);
        validateData(y);

        if (X.length != y.length) {
            throw new IllegalArgumentException("X and y must have same length");
        }
        
        if (testRatio < 0.0 || testRatio > 1.0) {
            throw new IllegalArgumentException("Test ratio must be between 0 and 1");
        }
        
        int n = X.length;
        int testSize = (int) (n * testRatio);
        int trainSize = n - testSize;
        
        // shuffle indices
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        shuffleArray(indices, seed);
        
        // split
        double[][] XTrain = new double[trainSize][];
        double[][] yTrain = new double[trainSize][];
        double[][] XTest = new double[testSize][];
        double[][] yTest = new double[testSize][];
        
        for (int i = 0; i < trainSize; i++) {
            XTrain[i] = X[indices[i]];
            yTrain[i] = y[indices[i]];
        }
        
        for (int i = 0; i < testSize; i++) {
            XTest[i] = X[indices[trainSize + i]];
            yTest[i] = y[indices[trainSize + i]];
        }
        
        return new DataSplit(XTrain, yTrain, XTest, yTest);
    }
    
    public static NormalizationResult minMaxNormalize(double[][] data) {
        validateData(data);
        
        int n = data.length;
        int features = data[0].length;
        
        double[] min = new double[features];
        double[] max = new double[features];
        
        for (int j = 0; j < features; j++) {
            min[j] = data[0][j];
            max[j] = data[0][j];
        }
        
        // min and max for each feature
        for (double[] d : data) {
            for (int j = 0; j < features; j++) {
                if (d[j] < min[j]) min[j] = d[j];
                if (d[j] > max[j]) max[j] = d[j];
            }
        }
        
        // Normalize
        double[][] normalized = new double[n][features];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < features; j++) {
                if (max[j] - min[j] != 0) {
                    normalized[i][j] = (data[i][j] - min[j]) / (max[j] - min[j]);
                } else {
                    normalized[i][j] = 0.0; // Handle constant features
                }
            }
        }
        
        return new NormalizationResult(normalized, min, max);
    }
    
    // min-max normalization using existing parameters
    public static double[][] applyMinMaxNormalization(double[][] data, double[] min, double[] max) {
        validateData(data);

        int n = data.length;
        int features = data[0].length;
        
        if (min.length != features || max.length != features) {
            throw new IllegalArgumentException("Min and max arrays must match data features");
        }
        
        double[][] normalized = new double[n][features];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < features; j++) {
                if (max[j] - min[j] != 0) {
                    normalized[i][j] = (data[i][j] - min[j]) / (max[j] - min[j]);
                } else {
                    normalized[i][j] = 0.0;
                }
            }
        }
        
        return normalized;
    }
    
    // z-score normalization (mean=0, std=1)
    public static NormalizationResult standardize(double[][] data) {
        validateData(data);
        
        int n = data.length;
        int features = data[0].length;
        
        double[] mean = new double[features];
        double[] std = new double[features];
        
        // mean
        for (double[] datum : data) {
            for (int j = 0; j < features; j++) {
                mean[j] += datum[j];
            }
        }
        for (int j = 0; j < features; j++) {
            mean[j] /= n;
        }
        
        // standard deviation
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < features; j++) {
                double diff = data[i][j] - mean[j];
                std[j] += diff * diff;
            }
        }
        for (int j = 0; j < features; j++) {
            std[j] = Math.sqrt(std[j] / n);
        }
        
        double[][] standardized = new double[n][features];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < features; j++) {
                if (std[j] != 0) {
                    standardized[i][j] = (data[i][j] - mean[j]) / std[j];
                } else {
                    standardized[i][j] = 0.0; // Handle constant features
                }
            }
        }
        
        return new NormalizationResult(standardized, mean, std);
    }
    
    // Validate input data
    public static void validateData(double[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }
        
        int features = data[0].length;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                throw new IllegalArgumentException("Data row " + i + " is null");
            }
            if (data[i].length != features) {
                throw new IllegalArgumentException("Inconsistent feature count at row " + i);
            }
            for (int j = 0; j < features; j++) {
                if (Double.isNaN(data[i][j]) || Double.isInfinite(data[i][j])) {
                    throw new IllegalArgumentException("Invalid value at row " + i + ", column " + j);
                }
            }
        }
    }
    
    // Shuffle array of indices
    private static void shuffleArray(int[] array, long seed) {
        Random random = new Random(seed);
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    public static class DataSplit {
        public final double[][] XTrain;
        public final double[][] yTrain;
        public final double[][] XTest;
        public final double[][] yTest;
        
        public DataSplit(double[][] XTrain, double[][] yTrain, double[][] XTest, double[][] yTest) {
            this.XTrain = XTrain;
            this.yTrain = yTrain;
            this.XTest = XTest;
            this.yTest = yTest;
        }
    }
    
    public static class NormalizationResult {
        public final double[][] data;
        public final double[] param1; // min or mean
        public final double[] param2; // max or std
        
        public NormalizationResult(double[][] data, double[] param1, double[] param2) {
            this.data = data;
            this.param1 = param1;
            this.param2 = param2;
        }
    }
}
