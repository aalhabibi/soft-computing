package neuralNetwork.casestudy;

import neuralNetwork.core.NetworkBuilder;
import neuralNetwork.core.NeuralNetwork;
import neuralNetwork.utils.*;
import neuralNetwork.casestudy.DatasetGenerator.WeatherData;
import neuralNetwork.activation.*;
import neuralNetwork.initialization.*;

import java.util.*;


public class OutfitRecommendation {
    
    private NeuralNetwork network;
    private DataUtils.NormalizationResult normalization;
    private final List<Double> lossHistory;
    private final List<Double> trainAccHistory;
    private final List<Double> testAccHistory;
    
    public OutfitRecommendation() {
        lossHistory = new ArrayList<>();
        trainAccHistory = new ArrayList<>();
        testAccHistory = new ArrayList<>();
    }


    public void buildNetwork(int[] hiddenLayers, String[] activations, double learningRate, String initMethod, String lossFunc) {
        System.out.println("\n=== Building Neural Network ===");
        System.out.println("Architecture:");
        System.out.println("  Input Layer:    4 neurons (temp, humidity, wind, rain)");
        
        for (int i = 0; i < hiddenLayers.length; i++) {
            System.out.printf("  Hidden Layer %d: %d neurons (%s activation)%n", 
                i + 1, hiddenLayers[i], activations[i].toUpperCase());
        }
        System.out.printf("  Output Layer:   3 neurons (%s activation)%n", 
            activations[activations.length - 1].toUpperCase());
        
        System.out.println("\nConfiguration:");
        System.out.printf("  Learning Rate: %.4f%n", learningRate);
        System.out.printf("  Initialization: %s%n", initMethod.toUpperCase());
        System.out.printf("  Loss Function: %s%n", lossFunc.toUpperCase());
        
        // Build network with custom configuration
        WeightInitializer initializer = initMethod.equalsIgnoreCase("random") 
            ? new RandomUniform() : new XavierInitializer();
        
        NetworkBuilder builder = new NetworkBuilder(4, initializer);
        
        // add  layers
        for (int i = 0; i < hiddenLayers.length; i++) {
            String activation = activations[i].toLowerCase();
            builder.addLayer(hiddenLayers[i], createActivation(activation));
        }
        
        String outputActivation = activations[activations.length - 1].toLowerCase();
        builder.addLayer(3, createActivation(outputActivation));
        
        // set loss function
        if (lossFunc.equalsIgnoreCase("crossentropy") || lossFunc.equalsIgnoreCase("cce")) {
            builder.categoricalCrossEntropyLoss();
        } else {
            builder.mseLoss();
        }
        
        network = builder.learningRate(learningRate).build();
        System.out.println("Network created successfully!");
    }

    public void buildNetwork(double learningRate) {
        int[] defaultHiddenLayers = {32, 16};
        String[] defaultActivations = {"relu", "relu", "sigmoid"};
        buildNetwork(defaultHiddenLayers, defaultActivations, learningRate, "xavier", "mse");
    }
    
    private ActivationFunction createActivation(String name) {
        return switch (name) {
            case "sigmoid" -> new Sigmoid();
            case "relu" -> new ReLU();
            case "tanh" -> new Tanh();
            case "linear" -> new Linear();
            default -> throw new IllegalArgumentException("Unknown activation: " + name);
        };
    }
    
    public void train(WeatherData trainData, WeatherData testData, int epochs, int batchSize) {
        System.out.println("\n=== Training Neural Network ===");
        System.out.println("Batch Size: " + batchSize);
        
        // normalize features
        System.out.println("Normalizing features...");
        normalization = DataUtils.minMaxNormalize(trainData.features);
        double[][] normalizedTest = DataUtils.applyMinMaxNormalization(
            testData.features, normalization.param1, normalization.param2
        );
        
        // convert labels to one-hot encoding
        double[][] trainLabels = trainData.getOneHotLabels();
        double[][] testLabels = testData.getOneHotLabels();
        
        System.out.println("Starting training for " + epochs + " epochs...\n");
        
        int checkpointInterval = Math.max(1, epochs / 20);
        int dataSize = normalization.data.length;
        
        // train with mini-batches
        for (int epoch = 0; epoch < epochs; epoch++) {
            double epochLoss = 0.0;
            int numBatches = 0;
            
            for (int i = 0; i < dataSize; i += batchSize) {
                int batchEnd = Math.min(i + batchSize, dataSize);
                int currentBatchSize = batchEnd - i;
                
                double[][] batchInputs = new double[currentBatchSize][];
                double[][] batchTargets = new double[currentBatchSize][];
                
                System.arraycopy(normalization.data, i, batchInputs, 0, currentBatchSize);
                System.arraycopy(trainLabels, i, batchTargets, 0, currentBatchSize);
                
                double batchLoss = network.trainBatch(batchInputs, batchTargets);
                epochLoss += batchLoss;
                numBatches++;
            }
            
            epochLoss /= numBatches;
            lossHistory.add(epochLoss);
            
            // training accuracy at checkpoints
            if ((epoch + 1) % checkpointInterval == 0 || epoch == 0 || epoch == epochs - 1) {
                double[][] trainPred = network.predict(normalization.data);
                double trainAcc = Metrics.accuracy(trainPred, trainLabels);
                trainAccHistory.add(trainAcc);
                
                // test at the end
                if (epoch == epochs - 1) {
                    double[][] testPred = network.predict(normalizedTest);
                    double testAcc = Metrics.accuracy(testPred, testLabels);
                    testAccHistory.add(testAcc);
                    System.out.printf("Epoch %4d/%d - Loss: %.6f - Train Acc: %.2f%% - Test Acc: %.2f%%%n",
                        epoch + 1, epochs, epochLoss, trainAcc * 100, testAcc * 100);
                } else {
                    System.out.printf("Epoch %4d/%d - Loss: %.6f - Train Acc: %.2f%%%n",
                        epoch + 1, epochs, epochLoss, trainAcc * 100);
                }
            }
        }
        
        System.out.println("\nTraining completed!");
    }
    
    public EvaluationResults evaluate(WeatherData testData) {
        System.out.println("\n=== Evaluating Model ===");
        
        double[][] normalizedTest = DataUtils.applyMinMaxNormalization(
            testData.features, normalization.param1, normalization.param2
        );
        
        double[][] predictions = network.predict(normalizedTest);
        double[][] testLabels = testData.getOneHotLabels();
        
        double accuracy = Metrics.accuracy(predictions, testLabels);
        
        int[][] confusionMatrix = new int[3][3];
        int[] trueCounts = new int[3];
        int[] predCounts = new int[3];

        for (int i = 0; i < predictions.length; i++) {
            int trueClass = argmax(testLabels[i]);
            int predClass = argmax(predictions[i]);

            confusionMatrix[trueClass][predClass]++;
            trueCounts[trueClass]++;
            predCounts[predClass]++;
        }

        double[] precision = new double[3];
        double[] recall = new double[3];
        double[] f1Score = new double[3];

        for (int c = 0; c < 3; c++) {
            precision[c] = predCounts[c] > 0 ?
                (double) confusionMatrix[c][c] / predCounts[c] : 0;
            recall[c] = trueCounts[c] > 0 ?
                (double) confusionMatrix[c][c] / trueCounts[c] : 0;
            f1Score[c] = (precision[c] + recall[c]) > 0 ?
                2 * precision[c] * recall[c] / (precision[c] + recall[c]) : 0;
        }
        
        System.out.println("\n📊 Overall Metrics:");
        System.out.printf("  Accuracy: %.2f%%%n", accuracy * 100);

        System.out.println("\n📊 Per-Class Metrics:");
        String[] classNames = {"Light", "Medium", "Heavy"};
        System.out.println("  Class    | Precision | Recall | F1-Score");
        System.out.println("  ---------|-----------|--------|----------");
        for (int c = 0; c < 3; c++) {
            System.out.printf("  %-8s | %.2f%%    | %.2f%% | %.4f%n",
                classNames[c], precision[c] * 100, recall[c] * 100, f1Score[c]);
        }

        System.out.println("\n📊 Confusion Matrix:");
        System.out.println("           Predicted");
        System.out.println("         Light Medium Heavy");
        System.out.println("       +------+------+------+");
        for (int t = 0; t < 3; t++) {
            System.out.printf("%-6s |", classNames[t]);
            for (int p = 0; p < 3; p++) {
                System.out.printf(" %4d |", confusionMatrix[t][p]);
            }
            System.out.println();
        }
        System.out.println("       +------+------+------+");
        
        return new EvaluationResults(accuracy, precision, recall, f1Score, confusionMatrix);
    }
    
    public int predictOutfit(double temperature, double humidity, double windSpeed, double rainfall) {
        double[][] input = {{temperature, humidity, windSpeed, rainfall}};
        double[][] normalized = DataUtils.applyMinMaxNormalization(
            input, normalization.param1, normalization.param2
        );
        
        double[] prediction = network.predict(normalized[0]);
        return argmax(prediction);
    }
    
    public TrainingHistory getTrainingHistory() {
        return new TrainingHistory(lossHistory, trainAccHistory, testAccHistory);
    }
    
    private int argmax(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    public static class EvaluationResults {
        public final double accuracy;
        public final double[] precision;
        public final double[] recall;
        public final double[] f1Score;
        public final int[][] confusionMatrix;
        
        public EvaluationResults(double accuracy, double[] precision,
                                double[] recall, double[] f1Score, int[][] confusionMatrix) {
            this.accuracy = accuracy;
            this.precision = precision;
            this.recall = recall;
            this.f1Score = f1Score;
            this.confusionMatrix = confusionMatrix;
        }
    }
    
    public static class TrainingHistory {
        public final List<Double> lossHistory;
        public final List<Double> trainAccHistory;
        public final List<Double> testAccHistory;
        
        public TrainingHistory(List<Double> loss, List<Double> trainAcc, List<Double> testAcc) {
            this.lossHistory = new ArrayList<>(loss);
            this.trainAccHistory = new ArrayList<>(trainAcc);
            this.testAccHistory = new ArrayList<>(testAcc);
        }
    }

}
