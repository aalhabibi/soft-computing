package geneticAlgorithm.carParts;

import geneticAlgorithm.chromosome.IntegerChromosome;

public class CarChromosome extends IntegerChromosome {
    private static final double MAX_COST = 15000;

    public CarChromosome(int length, int minValue, int maxValue) {
        super(length, minValue, maxValue);
    }

    @Override
    public double evaluateFitness() {
        // Check feasibility first
        if (!isFeasible()) {
            this.fitness = 0;
            return 0;
        }

        double perf = getTotalPerformance();
        double cost = getTotalCost();
        double compatibility = getCompatibilityScore();

        double base = perf * 0.7 + compatibility - cost * 0.01;
        this.fitness = Math.max(base, 0);

        return this.fitness;
    }



    public boolean isFeasible() {
        double cost = getTotalCost();
        if (cost > MAX_COST) {
            System.out.println("⚠️ Cost exceeded: " + cost + " (penalty applied)");
            return false;
        }
        return true;
    }


    // helpers


    private double getTotalPerformance() {
        CarPart engine = CarPartsDatabase.ENGINES.get((Integer) getGenes()[0]);
        CarPart wheels = CarPartsDatabase.WHEELS.get((Integer) getGenes()[1]);
        CarPart body = CarPartsDatabase.BODIES.get((Integer) getGenes()[2]);
        CarPart transmission = CarPartsDatabase.TRANSMISSIONS.get((Integer) getGenes()[3]);

        return engine.getPerformance() + wheels.getPerformance() +
                body.getPerformance() + transmission.getPerformance();
    }

    private double getTotalCost() {
        CarPart engine = CarPartsDatabase.ENGINES.get((Integer) getGenes()[0]);
        CarPart wheels = CarPartsDatabase.WHEELS.get((Integer) getGenes()[1]);
        CarPart body = CarPartsDatabase.BODIES.get((Integer) getGenes()[2]);
        CarPart transmission = CarPartsDatabase.TRANSMISSIONS.get((Integer) getGenes()[3]);

        return engine.getCost() + wheels.getCost() +
                body.getCost() + transmission.getCost();
    }

    private double getCompatibilityScore() {
        CarPart engine = CarPartsDatabase.ENGINES.get((Integer) getGenes()[0]);
        CarPart wheels = CarPartsDatabase.WHEELS.get((Integer) getGenes()[1]);
        CarPart body = CarPartsDatabase.BODIES.get((Integer) getGenes()[2]);
        CarPart transmission = CarPartsDatabase.TRANSMISSIONS.get((Integer) getGenes()[3]);

        double score = 0;
        if (engine.getName().contains("Sport") && transmission.getName().contains("Manual"))
            score += 30;
        if (body.getName().contains("SUV") && wheels.getName().contains("Off-road"))
            score += 25;
        if (engine.getName().contains("Eco") && body.getName().contains("Compact"))
            score += 20;

        return score;
    }
}
