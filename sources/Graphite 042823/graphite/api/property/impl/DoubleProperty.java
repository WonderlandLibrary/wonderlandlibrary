package graphite.api.property.impl;

import graphite.api.property.Property;
import graphite.api.util.math.MathUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public final class DoubleProperty extends Property<Double> {

    private final double min, max, inc;

    private final Representation representation;

    private final Map<Double, String> valueAliasMap = new HashMap<>();

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.####");

    public DoubleProperty(final String name, final double value, final double min, final double max, final double inc) {
        this(name, value, null, min, max, inc, Representation.DOUBLE);
    }

    public DoubleProperty(final String name, final double value, final double min, final double max, final double inc, final Representation representation) {
        this(name, value, null, min, max, inc, representation);
    }

    public DoubleProperty(final String name, final double value, final Dependency dependency, final double min, final double max, final double inc, final Representation representation) {
        super(name, value, dependency);

        this.min = min;
        this.max = max;
        this.inc = inc;

        this.representation = representation;
    }

    public String getDisplayString() {
        return this.valueAliasMap.containsKey(this.getValue()) ? this.valueAliasMap.get(this.getValue()) : DECIMAL_FORMAT.format(this.getValue());
    }

    public void addValueAlias(final double value, final String alias) {
        this.valueAliasMap.put(value, alias);
    }

    @Override
    public void setValue(Double value) {
        super.setValue(value > this.max ? this.max : value < this.min ? this.min : MathUtil.round(value, this.inc));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getInc() {
        return inc;
    }

    public Representation getRepresentation() {
        return representation;
    }
}