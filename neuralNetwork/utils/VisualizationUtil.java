package neuralNetwork.utils;

import neuralNetwork.casestudy.OutfitRecommendation.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VisualizationUtil {
    
    public static void plotLossCurve(List<Double> lossHistory) {
        if (lossHistory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No loss data available.");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Training Loss Curve");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            
            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int width = getWidth();
                    int height = getHeight();
                    int padding = 60;
                    int chartWidth = width - 2 * padding;
                    int chartHeight = height - 2 * padding;
                    
                    double minLoss = lossHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0);
                    double maxLoss = lossHistory.stream().mapToDouble(Double::doubleValue).max().orElse(1);
                    double range = maxLoss - minLoss;
                    if (range < 1e-10) range = 1;
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 18));
                    g2d.setColor(Color.BLACK);
                    FontMetrics fm = g2d.getFontMetrics();
                    String title = "Training Loss Curve";
                    g2d.drawString(title, (width - fm.stringWidth(title)) / 2, 30);
                    
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(padding, padding, padding, height - padding);
                    g2d.drawLine(padding, height - padding, width - padding, height - padding);
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    for (int i = 0; i <= 5; i++) {
                        double value = maxLoss - (range * i / 5.0);
                        int y = padding + (chartHeight * i / 5);
                        g2d.drawString(String.format("%.3f", value), padding - 50, y + 5);
                        g2d.drawLine(padding - 5, y, padding, y);
                    }
                    
                    for (int i = 0; i <= 5; i++) {
                        int epoch = lossHistory.size() * i / 5;
                        int x = padding + (chartWidth * i / 5);
                        g2d.drawString(String.valueOf(epoch), x - 10, height - padding + 20);
                        g2d.drawLine(x, height - padding, x, height - padding + 5);
                    }
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.drawString("Epochs", width / 2 - 20, height - 15);
                    g2d.rotate(-Math.PI / 2);
                    g2d.drawString("Loss", -height / 2 - 20, 20);
                    g2d.rotate(Math.PI / 2);
                    
                    g2d.setColor(new Color(30, 144, 255));
                    g2d.setStroke(new BasicStroke(2));
                    
                    for (int i = 0; i < lossHistory.size() - 1; i++) {
                        double loss1 = lossHistory.get(i);
                        double loss2 = lossHistory.get(i + 1);
                        
                        int x1 = padding + (int)((double)i / lossHistory.size() * chartWidth);
                        int y1 = padding + (int)((maxLoss - loss1) / range * chartHeight);
                        int x2 = padding + (int)((double)(i + 1) / lossHistory.size() * chartWidth);
                        int y2 = padding + (int)((maxLoss - loss2) / range * chartHeight);
                        
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                    
                    g2d.setColor(new Color(255, 69, 0));
                    for (int i = 0; i < lossHistory.size(); i++) {
                        double loss = lossHistory.get(i);
                        int x = padding + (int)((double)i / lossHistory.size() * chartWidth);
                        int y = padding + (int)((maxLoss - loss) / range * chartHeight);
                        g2d.fillOval(x - 3, y - 3, 6, 6);
                    }
                }
            };
            
            chartPanel.setBackground(Color.WHITE);
            frame.add(chartPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void plotAccuracyCurves(List<Double> trainAcc, List<Double> testAcc) {
        if (trainAcc.isEmpty() || testAcc.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No accuracy data available.");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Training & Testing Accuracy");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            
            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int width = getWidth();
                    int height = getHeight();
                    int padding = 60;
                    int bottomPadding = 120;
                    int chartWidth = width - 2 * padding;
                    int chartHeight = height - padding - bottomPadding;
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 18));
                    g2d.setColor(Color.BLACK);
                    FontMetrics fm = g2d.getFontMetrics();
                    String title = "Training & Testing Accuracy";
                    g2d.drawString(title, (width - fm.stringWidth(title)) / 2, 30);
                    
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(padding, padding, padding, padding + chartHeight);
                    g2d.drawLine(padding, padding + chartHeight, width - padding, padding + chartHeight);
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    for (int i = 0; i <= 5; i++) {
                        int percent = 100 - (i * 20);
                        int y = padding + (chartHeight * i / 5);
                        g2d.drawString(percent + "%", padding - 45, y + 5);
                        g2d.drawLine(padding - 5, y, padding, y);
                    }
                    
                    int maxCheckpoints = Math.max(trainAcc.size(), testAcc.size());
                    for (int i = 0; i <= 5; i++) {
                        int checkpoint = maxCheckpoints * i / 5;
                        int x = padding + (chartWidth * i / 5);
                        g2d.drawString(String.valueOf(checkpoint), x - 10, padding + chartHeight + 20);
                        g2d.drawLine(x, padding + chartHeight, x, padding + chartHeight + 5);
                    }
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.drawString("Checkpoints", width / 2 - 40, padding + chartHeight + 40);
                    g2d.rotate(-Math.PI / 2);
                    g2d.drawString("Accuracy", -(padding + chartHeight / 2) - 30, 20);
                    g2d.rotate(Math.PI / 2);
                    
                    g2d.setColor(new Color(30, 144, 255));
                    g2d.setStroke(new BasicStroke(2));
                    for (int i = 0; i < trainAcc.size() - 1; i++) {
                        double acc1 = trainAcc.get(i);
                        double acc2 = trainAcc.get(i + 1);
                        
                        int x1 = padding + (int)((double)i / maxCheckpoints * chartWidth);
                        int y1 = padding + (int)((1 - acc1) * chartHeight);
                        int x2 = padding + (int)((double)(i + 1) / maxCheckpoints * chartWidth);
                        int y2 = padding + (int)((1 - acc2) * chartHeight);
                        
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                    
                    g2d.setColor(new Color(255, 69, 0));
                    g2d.setStroke(new BasicStroke(2));
                    for (int i = 0; i < testAcc.size() - 1; i++) {
                        double acc1 = testAcc.get(i);
                        double acc2 = testAcc.get(i + 1);
                        
                        int x1 = padding + (int)((double)i / maxCheckpoints * chartWidth);
                        int y1 = padding + (int)((1 - acc1) * chartHeight);
                        int x2 = padding + (int)((double)(i + 1) / maxCheckpoints * chartWidth);
                        int y2 = padding + (int)((1 - acc2) * chartHeight);
                        
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                    
                    g2d.setColor(new Color(30, 144, 255));
                    for (int i = 0; i < trainAcc.size(); i++) {
                        double acc = trainAcc.get(i);
                        int x = padding + (int)((double)i / maxCheckpoints * chartWidth);
                        int y = padding + (int)((1 - acc) * chartHeight);
                        g2d.fillOval(x - 4, y - 4, 8, 8);
                    }
                    
                    g2d.setColor(new Color(255, 69, 0));
                    for (int i = 0; i < testAcc.size(); i++) {
                        double acc = testAcc.get(i);
                        int x = padding + (int)((double)i / maxCheckpoints * chartWidth);
                        int y = padding + (int)((1 - acc) * chartHeight);
                        
                        g2d.setStroke(new BasicStroke(2));
                        g2d.setColor(Color.WHITE);
                        g2d.fillOval(x - 6, y - 6, 12, 12);
                        g2d.setColor(new Color(255, 69, 0));
                        g2d.fillOval(x - 5, y - 5, 10, 10);
                        g2d.drawOval(x - 6, y - 6, 12, 12);
                    }
                    
                    int legendY = padding + chartHeight + 60;
                    int legendX = width / 2 - 120;
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    g2d.setColor(new Color(30, 144, 255));
                    g2d.fillOval(legendX - 4, legendY - 4, 8, 8);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Training Accuracy (tracked during training)", legendX + 10, legendY + 5);
                    
                    g2d.setColor(new Color(255, 69, 0));
                    g2d.fillOval(legendX - 8, legendY + 20 - 8, 16, 16);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(legendX - 10, legendY + 20 - 10, 20, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Test Accuracy (evaluated at end only)", legendX + 10, legendY + 25);
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    double finalTrain = trainAcc.getLast();
                    double finalTest = testAcc.getLast();
                    String finalText = String.format("Final Results → Train: %.2f%%, Test: %.2f%%", 
                                                    finalTrain * 100, finalTest * 100);
                    FontMetrics finalFm = g2d.getFontMetrics();
                    g2d.drawString(finalText, (width - finalFm.stringWidth(finalText)) / 2, legendY + 50);
                }
            };
            
            chartPanel.setBackground(Color.WHITE);
            frame.add(chartPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void plotConfusionMatrix(int[][] matrix) {
        String[] classes = {"Light", "Medium", "Heavy"};
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Confusion Matrix Heatmap");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 700);
            
            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int width = getWidth();

                    int maxVal = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            maxVal = Math.max(maxVal, matrix[i][j]);
                        }
                    }
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    g2d.setColor(Color.BLACK);
                    FontMetrics fm = g2d.getFontMetrics();
                    String title = "Confusion Matrix Heatmap";
                    g2d.drawString(title, (width - fm.stringWidth(title)) / 2, 40);
                    
                    int cellSize = 150;
                    int startX = (width - cellSize * 3) / 2;
                    int startY = 150;
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    String predictedLabel = "Predicted";
                    g2d.drawString(predictedLabel, (width - fm.stringWidth(predictedLabel)) / 2, startY - 80);
                    
                    g2d.rotate(-Math.PI / 2);
                    g2d.drawString("Actual", -(startY + cellSize * 3 / 2 + 20), startX - 80);
                    g2d.rotate(Math.PI / 2);
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    for (int j = 0; j < 3; j++) {
                        int x = startX + j * cellSize + cellSize / 2;
                        int y = startY - 20;
                        fm = g2d.getFontMetrics();
                        g2d.drawString(classes[j], x - fm.stringWidth(classes[j]) / 2, y);
                    }
                    
                    for (int i = 0; i < 3; i++) {
                        int x = startX - 60;
                        int y = startY + i * cellSize + cellSize / 2;
                        g2d.drawString(classes[i], x, y + 5);
                    }
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 24));
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            int x = startX + j * cellSize;
                            int y = startY + i * cellSize;
                            int val = matrix[i][j];
                            
                            double ratio = maxVal > 0 ? (double) val / maxVal : 0;
                            int intensity = (int)(ratio * 200);
                            Color cellColor = new Color(255 - intensity, 255 - intensity, 255);
                            
                            g2d.setColor(cellColor);
                            g2d.fillRect(x, y, cellSize, cellSize);
                            
                            g2d.setColor(Color.BLACK);
                            g2d.setStroke(new BasicStroke(2));
                            g2d.drawRect(x, y, cellSize, cellSize);
                            
                            String valStr = String.valueOf(val);
                            fm = g2d.getFontMetrics();
                            int textX = x + (cellSize - fm.stringWidth(valStr)) / 2;
                            int textY = y + (cellSize + fm.getHeight()) / 2 - 5;
                            g2d.drawString(valStr, textX, textY);
                        }
                    }
                    
                    int legendX = startX;
                    int legendY = startY + cellSize * 3 + 40;
                    int legendWidth = cellSize * 3;
                    int legendHeight = 30;
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2d.drawString("Color Scale:", legendX, legendY - 10);
                    
                    for (int i = 0; i < legendWidth; i++) {
                        double ratio = (double) i / legendWidth;
                        int intensity = (int)(ratio * 200);
                        g2d.setColor(new Color(255 - intensity, 255 - intensity, 255));
                        g2d.fillRect(legendX + i, legendY, 1, legendHeight);
                    }
                    
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(legendX, legendY, legendWidth, legendHeight);
                    g2d.drawString("0", legendX - 5, legendY + legendHeight + 15);
                    g2d.drawString(String.valueOf(maxVal), legendX + legendWidth - 10, legendY + legendHeight + 15);
                }
            };
            
            chartPanel.setBackground(Color.WHITE);
            frame.add(chartPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void printSummaryTable(EvaluationResults results) {
        String[] classes = {"Light", "Medium", "Heavy"};
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Performance Summary Table");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(700, 500);
            
            JPanel tablePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int width = getWidth();
                    int startX = 50;
                    int startY = 80;
                    int rowHeight = 40;
                    int[] colWidths = {120, 100, 100, 100, 100};
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    g2d.setColor(Color.BLACK);
                    FontMetrics fm = g2d.getFontMetrics();
                    String title = "Performance Summary Table";
                    g2d.drawString(title, (width - fm.stringWidth(title)) / 2, 40);
                    
                    int totalSupport = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            totalSupport += results.confusionMatrix[i][j];
                        }
                    }
                    
                    double avgPrecision = 0, avgRecall = 0, avgF1 = 0;
                    for (int i = 0; i < 3; i++) {
                        int support = 0;
                        for (int j = 0; j < 3; j++) {
                            support += results.confusionMatrix[i][j];
                        }
                        double weight = (double) support / totalSupport;
                        avgPrecision += results.precision[i] * weight;
                        avgRecall += results.recall[i] * weight;
                        avgF1 += results.f1Score[i] * weight;
                    }
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.setColor(new Color(70, 130, 180));
                    int x = startX;
                    g2d.fillRect(x, startY, colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], rowHeight);
                    
                    g2d.setColor(Color.WHITE);
                    String[] headers = {"Class", "Precision", "Recall", "F1-Score", "Support"};
                    x = startX;
                    for (int i = 0; i < headers.length; i++) {
                        fm = g2d.getFontMetrics();
                        g2d.drawString(headers[i], x + (colWidths[i] - fm.stringWidth(headers[i])) / 2, startY + 25);
                        x += colWidths[i];
                    }
                    
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    x = startX;
                    for (int i = 0; i <= headers.length; i++) {
                        g2d.drawLine(x, startY, x, startY + rowHeight);
                        if (i < headers.length) x += colWidths[i];
                    }
                    g2d.drawLine(startX, startY, startX + colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], startY);
                    g2d.drawLine(startX, startY + rowHeight, startX + colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], startY + rowHeight);
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 13));
                    g2d.setColor(Color.BLACK);
                    
                    for (int i = 0; i < 3; i++) {
                        int y = startY + (i + 1) * rowHeight;
                        
                        if (i % 2 == 0) {
                            g2d.setColor(new Color(240, 248, 255));
                            g2d.fillRect(startX, y, colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], rowHeight);
                            g2d.setColor(Color.BLACK);
                        }
                        
                        int support = 0;
                        for (int j = 0; j < 3; j++) {
                            support += results.confusionMatrix[i][j];
                        }
                        
                        String[] rowData = {
                            classes[i],
                            String.format("%.2f%%", results.precision[i] * 100),
                            String.format("%.2f%%", results.recall[i] * 100),
                            String.format("%.4f", results.f1Score[i]),
                            String.valueOf(support)
                        };
                        
                        x = startX;
                        for (int j = 0; j < rowData.length; j++) {
                            fm = g2d.getFontMetrics();
                            g2d.drawString(rowData[j], x + (colWidths[j] - fm.stringWidth(rowData[j])) / 2, y + 25);
                            x += colWidths[j];
                        }
                        
                        g2d.setStroke(new BasicStroke(1));
                        x = startX;
                        for (int j = 0; j <= headers.length; j++) {
                            g2d.drawLine(x, y, x, y + rowHeight);
                            if (j < headers.length) x += colWidths[j];
                        }
                        g2d.drawLine(startX, y + rowHeight, startX + colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], y + rowHeight);
                    }
                    
                    int y = startY + 4 * rowHeight;
                    g2d.setColor(new Color(255, 250, 205));
                    g2d.fillRect(startX, y, colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], rowHeight);
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 13));
                    g2d.setColor(Color.BLACK);
                    String[] avgRow = {
                        "Weighted",
                        String.format("%.2f%%", avgPrecision * 100),
                        String.format("%.2f%%", avgRecall * 100),
                        String.format("%.4f", avgF1),
                        String.valueOf(totalSupport)
                    };
                    
                    x = startX;
                    for (int j = 0; j < avgRow.length; j++) {
                        fm = g2d.getFontMetrics();
                        g2d.drawString(avgRow[j], x + (colWidths[j] - fm.stringWidth(avgRow[j])) / 2, y + 25);
                        x += colWidths[j];
                    }
                    
                    g2d.setStroke(new BasicStroke(2));
                    x = startX;
                    for (int j = 0; j <= headers.length; j++) {
                        g2d.drawLine(x, y, x, y + rowHeight);
                        if (j < headers.length) x += colWidths[j];
                    }
                    g2d.drawLine(startX, y + rowHeight, startX + colWidths[0] + colWidths[1] + colWidths[2] + colWidths[3] + colWidths[4], y + rowHeight);
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    String accuracyText = String.format("Overall Accuracy: %.2f%%", results.accuracy * 100);
                    fm = g2d.getFontMetrics();
                    g2d.drawString(accuracyText, (width - fm.stringWidth(accuracyText)) / 2, y + rowHeight + 50);
                }
            };
            
            tablePanel.setBackground(Color.WHITE);
            frame.add(tablePanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
