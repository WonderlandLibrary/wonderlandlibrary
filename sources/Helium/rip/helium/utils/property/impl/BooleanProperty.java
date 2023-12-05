package rip.helium.utils.property.impl;

import rip.helium.Helium;
import rip.helium.event.helium.UpdateValueEvent;
import rip.helium.utils.Parser;
import rip.helium.utils.property.abs.Property;

/**
 * @author antja03
 */
public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String id, String description, rip.helium.utils.Dependency dependency, Boolean defaultValue) {
        super(id, description, dependency);
        this.value = defaultValue;
    }

    @Override
    public void setValue(String input) {
        Boolean newValue = Parser.parseBool(input);
        if (newValue != null && value != newValue) {
            Helium.eventBus.publish(new UpdateValueEvent(this, getValue(), newValue));
            value = newValue;
        }
    }

}

