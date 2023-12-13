package vip.radium.property.impl;

import vip.radium.property.Property;

import java.util.function.Supplier;

public class DoubleProperty extends Property<Double> {

    private final double min;
    private final double max;
    private final double increment;
    private final Representation representation;

    public DoubleProperty(String label, double value, Supplier<Boolean> dependency, double min, double max, double increment, Representation representation) {
        super(label, value, dependency);
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.representation = representation;
    }

    public DoubleProperty(String label, double value, Supplier<Boolean> dependency, double min, double max, double increment) {
        this(label, value, dependency, min, max, increment, Representation.DOUBLE);
    }

    public DoubleProperty(String label, double value, double min, double max, double increment, Representation representation) {
        this(label, value, () -> true, min, max, increment, representation);
    }

    public DoubleProperty(String label, double value, double min, double max, double increment) {
        this(label, value, () -> true, min, max, increment, Representation.DOUBLE);
    }

    public Representation getRepresentation() {
        return representation;
    }

    @Override
    public void setValue(Double value) {
        if (this.value != null && this.value.doubleValue() != value.doubleValue()) {
            if (value < min)
                value = min;
            else if (value > max)
                value = max;
        }

        super.setValue(value);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }
}
