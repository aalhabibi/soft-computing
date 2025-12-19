package neuralNetwork.casestudy;

import java.io.*;
import java.util.*;

public class DatasetGenerator {
    
    private final Random random;
    
    public DatasetGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    // Generate weather dataset
    public WeatherData generateDataset(int numSamples) {
        double[][] features = new double[numSamples][4];
        int[] labels = new int[numSamples];
        
        for (int i = 0; i < numSamples; i++) {
            // Decide outfit category
            int category = random.nextInt(3);
            
            // Generate weather features based on category
            if (category == 0) {
                features[i][0] = 25 + random.nextGaussian() * 5; // temp: 20-30°C
                features[i][1] = 30 + random.nextGaussian() * 10; // humidity: 20-40%
                features[i][2] = 5 + random.nextGaussian() * 3; // wind: 2-8 km/h
                features[i][3] = random.nextGaussian() * 1; // rain: 0-1 mm
            } else if (category == 1) {
                features[i][0] = 15 + random.nextGaussian() * 5; // temp: 10-20°C
                features[i][1] = 50 + random.nextGaussian() * 15; // humidity: 35-65%
                features[i][2] = 15 + random.nextGaussian() * 5; // wind: 10-20 km/h
                features[i][3] = 2 + random.nextGaussian() * 2; // rain: 0-4 mm
            } else {
                features[i][0] = 5 + random.nextGaussian() * 5; // temp: 0-10°C
                features[i][1] = 70 + random.nextGaussian() * 15; // humidity: 55-85%
                features[i][2] = 25 + random.nextGaussian() * 8; // wind: 17-33 km/h
                features[i][3] = 5 + random.nextGaussian() * 3; // rain: 2-8 mm
            }
            
            // Ensure values are in reasonable ranges
            features[i][0] = Math.max(-10, Math.min(40, features[i][0])); // temp
            features[i][1] = Math.max(0, Math.min(100, features[i][1])); // humidity
            features[i][2] = Math.max(0, Math.min(50, features[i][2])); // wind
            features[i][3] = Math.max(0, Math.min(20, features[i][3])); // rain
            
            labels[i] = category;
        }
        
        return new WeatherData(features, labels);
    }
    
    public void saveToCSV(WeatherData data, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("temperature,humidity,wind_speed,rainfall,outfit_category");
            
            for (int i = 0; i < data.features.length; i++) {
                writer.printf("%.2f,%.2f,%.2f,%.2f,%d%n",
                    data.features[i][0], data.features[i][1], data.features[i][2], data.features[i][3], data.labels[i]);
            }
        }
    }
    
    public static WeatherData loadFromCSV(String filename) throws IOException {
        List<double[]> featuresList = new ArrayList<>();
        List<Integer> labelsList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                double[] features = new double[4];
                features[0] = Double.parseDouble(parts[0]); // temperature
                features[1] = Double.parseDouble(parts[1]); // humidity
                features[2] = Double.parseDouble(parts[2]); // wind
                features[3] = Double.parseDouble(parts[3]); // rainfall
                
                int label = Integer.parseInt(parts[4]);
                
                featuresList.add(features);
                labelsList.add(label);
            }
        }
        
        // Convert to arrays
        double[][] features = featuresList.toArray(new double[0][]);
        int[] labels = new int[labelsList.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = labelsList.get(i);
        }
        
        return new WeatherData(features, labels);
    }

    public static class WeatherData {
        public final double[][] features;
        public final int[] labels;

        public WeatherData(double[][] features, int[] labels) {
            this.features = features;
            this.labels = labels;
        }

        // convert integer labels
        public double[][] getOneHotLabels() {
            double[][] oneHot = new double[labels.length][3];
            for (int i = 0; i < labels.length; i++) {
                oneHot[i][labels[i]] = 1.0;
            }
            return oneHot;
        }

        public static String getLabelName(int label) {
            return switch (label) {
                case 0 -> "Light";
                case 1 -> "Medium";
                case 2 -> "Heavy";
                default -> "Unknown";
            };
        }

        public void printStatistics() {
            System.out.println("\n=== Dataset Statistics ===");
            System.out.println("Total samples: " + features.length);

            int[] counts = new int[3];
            for (int label : labels) {
                counts[label]++;
            }

            System.out.println("\nClass Distribution:");
            System.out.printf("  Light (0):  %d samples (%.1f%%)%n", counts[0], 100.0 * counts[0] / labels.length);
            System.out.printf("  Medium (1): %d samples (%.1f%%)%n", counts[1], 100.0 * counts[1] / labels.length);
            System.out.printf("  Heavy (2):  %d samples (%.1f%%)%n", counts[2], 100.0 * counts[2] / labels.length);

            System.out.println("\nFeature Ranges:");
            String[] featureNames = {"Temperature (°C)", "Humidity (%)", "Wind Speed (km/h)", "Rainfall (mm)"};

            for (int f = 0; f < 4; f++) {
                double min = features[0][f];
                double max = features[0][f];
                double sum = 0;

                for (double[] feature : features) {
                    double val = feature[f];
                    min = Math.min(min, val);
                    max = Math.max(max, val);
                    sum += val;
                }

                double mean = sum / features.length;
                System.out.printf("  %s: %.2f - %.2f (mean: %.2f)%n", featureNames[f], min, max, mean);
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("=== Weather Dataset Generator ===\n");
            
            DatasetGenerator generator = new DatasetGenerator(42);
            WeatherData data = generator.generateDataset(1000);
            
            data.printStatistics();
            
            String filename = "weather_outfit_dataset.csv";
            generator.saveToCSV(data, filename);
            System.out.println("\n✅ Dataset saved to: " + filename);
            
            WeatherData loaded = loadFromCSV(filename);
            System.out.println("✅ Dataset loaded successfully!");
            System.out.println("   Loaded " + loaded.features.length + " samples");
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
