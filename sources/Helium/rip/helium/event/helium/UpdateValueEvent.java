package rip.helium.event.helium;

import rip.helium.utils.property.abs.Property;

/**
 * @author antja03
 */
public class UpdateValueEvent {
    private final Property value;

    private final Object oldValue;
    private final Object newValue;

    public UpdateValueEvent(Property value, Object oldValue, Object newValue) {
        this.value = value;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Property getValue() {
        return value;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
