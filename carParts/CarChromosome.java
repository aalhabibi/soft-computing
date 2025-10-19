package carParts;

import chromosome.IntegerChromosome;

public class CarChromosome extends IntegerChromosome {
    public CarChromosome(int length, int minValue, int maxValue) {
        super(length, minValue, maxValue);
    }

    @Override
    public double evaluateFitness() {
        CarPart engine = CarPartsDatabase.ENGINES.get(getGenes()[0]);
        CarPart wheels = CarPartsDatabase.WHEELS.get(getGenes()[1]);
        CarPart body = CarPartsDatabase.BODIES.get(getGenes()[2]);
        CarPart transmission = CarPartsDatabase.TRANSMISSIONS.get(getGenes()[3]);

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
        return base;
    }

}
