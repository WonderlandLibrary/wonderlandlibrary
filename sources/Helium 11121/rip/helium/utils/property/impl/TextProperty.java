package rip.helium.utils.property.impl;

import rip.helium.Helium;
import rip.helium.event.helium.UpdateValueEvent;
import rip.helium.utils.property.abs.Property;

/**
 * @author antja03
 */
public class TextProperty extends Property<String> {

    private double minimum;
    private double maximum;
    private double increment;
    private String numType;

    public TextProperty(String id, String description, rip.helium.utils.Dependency dependency, String defaultValue) {
        super(id, description, dependency);
        this.value = defaultValue;
        this.maximum = maximum;
        this.increment = increment;
        this.numType = numType == null ? "" : numType;
    }

    @Override
    public void setValue(String input) {
        String oldValue = getValue();
        String newValue = input;

        value = newValue;

        Helium.eventBus.publish(new UpdateValueEvent(this, oldValue, getValue()));

    }
}
