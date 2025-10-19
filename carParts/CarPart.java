package carParts;

public class CarPart {
    private String name;
    private double cost;
    private double performance;

    public CarPart(String name, double cost, double performance) {
        this.name = name;
        this.cost = cost;
        this.performance = performance;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public double getPerformance() {
        return performance;
    }

    @Override
    public String toString() {
        return name + " (cost=" + cost + ", perf=" + performance + ")";
    }
}
