package club.dortware.client.property.impl;

import club.dortware.client.property.Property;

public final class DoubleProperty<T1> extends Property<T1, Double> {

    private final double min, max;
    private boolean isInt;
    public DoubleProperty(String name, T1 owner, double value, double min, double max) {
        super(name, owner, value);
        this.min = min;
        this.max = max;
    }

    public DoubleProperty(String name, T1 owner, double value, double min, double max, boolean isInt) {
        this(name, owner, value, min, max);
        this.isInt = isInt;
    }
    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    @Override
    public Double getValue() {
        if (isInt) {
            return (double) super.getValue().intValue();
        }
        return super.getValue();
    }

    @Override
    public void setValue(Double value) {
        super.setValue(Math.min(Math.max(min, value), max));
    }

    @Override
    public boolean equals(Object o) {
        return getValue().equals(o);
    }

    public boolean isInteger() {
        return isInt;
    }
}
