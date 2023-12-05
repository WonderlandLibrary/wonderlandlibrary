package rip.helium.utils.property.impl;

import org.apache.commons.lang3.math.NumberUtils;
import rip.helium.Helium;
import rip.helium.event.helium.UpdateValueEvent;
import rip.helium.utils.property.abs.Property;

/**
 * @author antja03
 */
public class DoubleProperty extends Property<Double> {

    private final double minimum;
    private final double maximum;
    private final double increment;
    private final String numType;

    public DoubleProperty(String id, String description, rip.helium.utils.Dependency dependency, double defaultValue, double minimum, double maximum, double increment, String numType) {
        super(id, description, dependency);
        this.value = defaultValue;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.numType = numType == null ? "" : numType;
    }

    @Override
    public void setValue(String input) {
        if (NumberUtils.isNumber(input)) {
            double oldValue = getValue();
            double newValue = Double.parseDouble(input);

            if (newValue < minimum) {
                newValue = minimum;
            } else if (newValue > maximum) {
                newValue = maximum;
            }

            value = newValue;

            Helium.eventBus.publish(new UpdateValueEvent(this, oldValue, getValue()));
        }
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getIncrement() {
        return increment;
    }

    public String getNumType() {
        return numType;
    }
}
