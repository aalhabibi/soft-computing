package fuzzylogic.operators;

public class OrMax implements BinaryOperator {
    @Override
    public double apply(double a, double b) {
        return Math.max(a, b);
    }
}
