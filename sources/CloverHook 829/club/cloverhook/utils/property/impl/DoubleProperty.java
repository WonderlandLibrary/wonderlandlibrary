package club.cloverhook.utils.property.impl;

import org.apache.commons.lang3.math.NumberUtils;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.cloverhook.UpdateValueEvent;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public class DoubleProperty extends Property<Double> {

    private double minimum;
    private double maximum;
    private double increment;
    private String numType;

    public DoubleProperty(String id, String description, club.cloverhook.utils.Dependency dependency, double defaultValue, double minimum, double maximum, double increment, String numType) {
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

            Cloverhook.eventBus.publish(new UpdateValueEvent(this, oldValue, getValue()));
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
