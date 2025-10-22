package carParts;

import chromosome.IntegerChromosome;

public class CarChromosome extends IntegerChromosome {
    public CarChromosome(int length, int minValue, int maxValue) {
        super(length, minValue, maxValue);
    }

    @Override
    public double evaluateFitness() {
        CarPart engine = CarPartsDatabase.ENGINES.get((Integer) getGenes()[0]);
        CarPart wheels = CarPartsDatabase.WHEELS.get((Integer) getGenes()[1]);
        CarPart body = CarPartsDatabase.BODIES.get((Integer) getGenes()[2]);
        CarPart transmission = CarPartsDatabase.TRANSMISSIONS.get((Integer) getGenes()[3]);

        double perf = engine.getPerformance() + wheels.getPerformance() +
                body.getPerformance() + transmission.getPerformance();
        double cost = engine.getCost() + wheels.getCost() +
                body.getCost() + transmission.getCost();

        // Compatibility score
        double compatibility = 0;
        if (engine.getName().contains("Sport") && transmission.getName().contains("Manual"))
            compatibility += 30;
        if (body.getName().contains("SUV") && wheels.getName().contains("Off-road"))
            compatibility += 25;
        if (engine.getName().contains("Eco") && body.getName().contains("Compact"))
            compatibility += 20;

        double base = perf * 0.7 + compatibility - cost * 0.01;

        // Apply penalization for exceeding cost limit
//        double MAX_COST = 10000;
//        double PENALTY_FACTOR = 0.50;
//        if (cost > MAX_COST) {
//            double penalty = (cost - MAX_COST) * PENALTY_FACTOR;
//            base -= penalty;
//        }
//        System.out.println("Chromosome: " + this + " | Total cost = " + cost);
//
        double MAX_COST = 10000;
        if (cost > MAX_COST) {
            System.out.println("⚠️ Cost exceeded: " + cost + " (penalty applied)");
            return 0; // infeasible solution
        }

        return Math.max(base, 0); // ensure fitness doesn't go negative
    }


}
