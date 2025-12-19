package neuralNetwork.casestudy;

import neuralNetwork.utils.DataUtils;
import neuralNetwork.casestudy.DatasetGenerator.WeatherData;
import neuralNetwork.casestudy.OutfitRecommendation.*;
import neuralNetwork.utils.VisualizationUtil;

import java.io.IOException;
import java.util.Scanner;

public class InteractiveDemo {
    
    private OutfitRecommendation model;
    private WeatherData trainData;
    private WeatherData testData;
    private final Scanner scanner;
    
    public InteractiveDemo() {
        this.scanner = new Scanner(System.in);
    }
    
    // initialize and train
    public void initialize() throws IOException {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            Weather-Based Outfit Recommendation            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Initializing...");
        
        // prepare dataset
        DatasetGenerator generator = new DatasetGenerator(42);
        WeatherData fullData = generator.generateDataset(1000);
        
        DataUtils.DataSplit split = DataUtils.trainTestSplit(
            fullData.features, fullData.getOneHotLabels(), 0.2, 42
        );
        
        int[] trainLabels = new int[split.yTrain.length];
        int[] testLabels = new int[split.yTest.length];
        
        for (int i = 0; i < trainLabels.length; i++) {
            trainLabels[i] = argmax(split.yTrain[i]);
        }
        for (int i = 0; i < testLabels.length; i++) {
            testLabels[i] = argmax(split.yTest[i]);
        }
        
        trainData = new WeatherData(split.XTrain, trainLabels);
        testData = new WeatherData(split.XTest, testLabels);
        
        // get hyperparameters from user
        System.out.print("\n\nWould you like to customize network configuration? (y/n) [default: n]: ");
        String customize = scanner.nextLine().trim().toLowerCase();
        
        if (customize.equals("y") || customize.equals("yes")) {
            initializeWithCustomConfig();
        } else {
            initializeWithDefaults();
        }
        
        EvaluationResults results = model.evaluate(testData);
        
        TrainingHistory history = model.getTrainingHistory();
        VisualizationUtil.plotLossCurve(history.lossHistory);
        VisualizationUtil.plotAccuracyCurves(history.trainAccHistory, history.testAccHistory);
        VisualizationUtil.plotConfusionMatrix(results.confusionMatrix);
        VisualizationUtil.printSummaryTable(results);
        
        System.out.println("\nModel ready for predictions");
    }
    
    private void initializeWithDefaults() {
        double learningRate = 0.05;
        int epochs = 500;
        int batchSize = 32;
        
        model = new neuralNetwork.casestudy.OutfitRecommendation();
        model.buildNetwork(learningRate);
        
        System.out.println("\nTraining model");
        model.train(trainData, testData, epochs, batchSize);
    }
    
    private void initializeWithCustomConfig() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            CUSTOM NETWORK CONFIGURATION                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        try {
            System.out.print("Number of hidden layers (1-5) [default: 2]: ");
            String layersInput = scanner.nextLine().trim();
            int numLayers = layersInput.isEmpty() ? 2 : Integer.parseInt(layersInput);
            numLayers = Math.max(1, Math.min(5, numLayers));
            
            int[] hiddenLayers = new int[numLayers];
            String[] activations = new String[numLayers + 1];
            
            // configure each layer
            for (int i = 0; i < numLayers; i++) {
                System.out.print("Hidden Layer " + (i + 1) + " neurons [default: " + (32 / (i + 1)) + "]: ");
                String neuronsInput = scanner.nextLine().trim();
                hiddenLayers[i] = neuronsInput.isEmpty() ? 32 / (i + 1) : Integer.parseInt(neuronsInput);
                
                System.out.print("Hidden Layer " + (i + 1) + " activation (relu/sigmoid/tanh/linear) [default: relu]: ");
                String actInput = scanner.nextLine().trim();
                activations[i] = actInput.isEmpty() ? "relu" : actInput.toLowerCase();
            }
            
            System.out.print("Output layer activation (sigmoid/tanh/linear) [default: sigmoid]: ");
            String outputAct = scanner.nextLine().trim();
            activations[numLayers] = outputAct.isEmpty() ? "sigmoid" : outputAct.toLowerCase();
            
            // get parameters
            System.out.print("\nLearning rate (0.001-10.0) [default: 0.05]: ");
            String lrInput = scanner.nextLine().trim();
            double learningRate = lrInput.isEmpty() ? 0.5 : Double.parseDouble(lrInput);
            learningRate = Math.max(0.001, Math.min(10.0, learningRate));
            
            System.out.print("Epochs (1-10000) [default: 500]: ");
            String epochsInput = scanner.nextLine().trim();
            int epochs = epochsInput.isEmpty() ? 500 : Integer.parseInt(epochsInput);
            epochs = Math.max(1, Math.min(10000, epochs));
            
            System.out.print("Batch size (1-256) [default: 32]: ");
            String batchInput = scanner.nextLine().trim();
            int batchSize = batchInput.isEmpty() ? 32 : Integer.parseInt(batchInput);
            batchSize = Math.max(1, Math.min(256, batchSize));
            
            System.out.print("Weight initialization (xavier/random) [default: xavier]: ");
            String initInput = scanner.nextLine().trim();
            String initMethod = initInput.isEmpty() ? "xavier" : initInput.toLowerCase();
            
            System.out.print("Loss function (mse/crossentropy) [default: mse]: ");
            String lossInput = scanner.nextLine().trim();
            String lossFunc = lossInput.isEmpty() ? "mse" : lossInput.toLowerCase();
            
            // build and train
            model = new neuralNetwork.casestudy.OutfitRecommendation();
            model.buildNetwork(hiddenLayers, activations, learningRate, initMethod, lossFunc);
            
            System.out.println("\nTraining model...");
            model.train(trainData, testData, epochs, batchSize);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default configuration.");
            initializeWithDefaults();
        }
    }

    public void runInteractive() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              INTERACTIVE PREDICTION MODE                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Enter weather conditions to get outfit recommendations.");
        System.out.println("Type 'quit' to exit.\n");
        
        while (true) {
            try {
                System.out.println("\n" + "─".repeat(60));
                System.out.println("Enter weather conditions:");
                System.out.println("─".repeat(60));
                
                System.out.print("Temperature (°C) [-10 to 40]: ");
                String tempInput = scanner.nextLine().trim();
                if (tempInput.equalsIgnoreCase("quit")) break;
                double temperature = Double.parseDouble(tempInput);
                
                System.out.print("Humidity (%) [0 to 100]: ");
                String humInput = scanner.nextLine().trim();
                if (humInput.equalsIgnoreCase("quit")) break;
                double humidity = Double.parseDouble(humInput);
                
                System.out.print("Wind Speed (km/h) [0 to 50]: ");
                String windInput = scanner.nextLine().trim();
                if (windInput.equalsIgnoreCase("quit")) break;
                double windSpeed = Double.parseDouble(windInput);
                
                System.out.print("Rainfall (mm) [0 to 20]: ");
                String rainInput = scanner.nextLine().trim();
                if (rainInput.equalsIgnoreCase("quit")) break;
                double rainfall = Double.parseDouble(rainInput);
                
                int prediction = model.predictOutfit(temperature, humidity, windSpeed, rainfall);
                String outfit = WeatherData.getLabelName(prediction);
                
                System.out.println("\n" + "═".repeat(60));
                System.out.println("  Weather Summary:");
                System.out.printf("  Temperature: %.1f°C%n", temperature);
                System.out.printf("  Humidity: %.1f%%%n", humidity);
                System.out.printf("  Wind Speed: %.1f km/h%n", windSpeed);
                System.out.printf("  Rainfall: %.1f mm%n", rainfall);
                
                System.out.println("\n Recommended Outfit: " + outfit.toUpperCase());
                
                switch (prediction) {
                    case 0:
                        System.out.println("  💡 Suggestion: T-shirt, shorts, light clothes");
                        break;
                    case 1:
                        System.out.println("  💡 Suggestion: Long sleeves, jeans, light jacket");
                        break;
                    case 2:
                        System.out.println("  💡 Suggestion: Heavy coat, warm layers, winter gear");
                        break;
                }
                System.out.println("═".repeat(60));
                
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter numeric values.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }

    public void runTestCases() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                        TEST CASES                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        double[][] testCases = {
            {28, 35, 5, 0, 0},      // Hot summer day -> Light
            {15, 50, 15, 2, 1},     // Cool spring day -> Medium
            {5, 75, 25, 5, 2},      // Cold winter day -> Heavy
            {20, 60, 10, 1, 1},     // Mild day -> Medium
        };
        
        String[] scenarios = {
            "☀ Hot summer day",
            "🌤 Cool spring day",
            "❄ Cold winter day",
            "🌤 Mild pleasant day"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            int prediction = model.predictOutfit(
                testCases[i][0], testCases[i][1], 
                testCases[i][2], testCases[i][3]
            );
            
            String outfit = WeatherData.getLabelName(prediction);
            int expected = (int) testCases[i][4];
            String expectedOutfit = WeatherData.getLabelName(expected);
            
            String status = (prediction == expected) ? "✅" : "❌";
            
            System.out.printf("%s %s%n", status, scenarios[i]);
            System.out.printf("   Weather: %.0f°C, %.0f%% humidity, %.0f km/h wind, %.0f mm rain%n",
                testCases[i][0], testCases[i][1], testCases[i][2], testCases[i][3]);
            System.out.printf("   Predicted: %-8s | Expected: %-8s%n%n", outfit, expectedOutfit);
        }
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

    public static void main(String[] args) {
        try {
            InteractiveDemo demo = new InteractiveDemo();
            
            demo.initialize();
            
            demo.runTestCases();
            
            // interactive mode
            System.out.println("\n" + "═".repeat(60));
            System.out.print("Would you like to try interactive mode? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("y") || response.equals("yes")) {
                demo.runInteractive();
            } else {
                System.out.println("\nDemo completed. Thank you!");
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
