package club.cloverhook.utils.property.impl;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.cloverhook.UpdateValueEvent;
import club.cloverhook.utils.Parser;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String id, String description, club.cloverhook.utils.Dependency dependency, Boolean defaultValue) {
        super(id, description, dependency);
        this.value = defaultValue;
    }

    @Override
    public void setValue(String input) {
        Boolean newValue = Parser.parseBool(input);
        if (newValue != null && value != newValue) {
        	Cloverhook.eventBus.publish(new UpdateValueEvent(this, getValue(), newValue));
            value = newValue;
        }
    }

}

