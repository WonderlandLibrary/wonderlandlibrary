package tech.atani.client.feature.value.impl;

import com.google.common.base.Supplier;
import tech.atani.client.feature.value.impl.bool.BooleanParser;
import tech.atani.client.feature.value.interfaces.ValueChangeListener;
import tech.atani.client.feature.value.Value;

import java.util.Optional;

public class CheckBoxValue extends Value<Boolean> {

    public CheckBoxValue(String label, String description, Object owner, Boolean value, ValueChangeListener[] valueChangeListeners, Supplier<Boolean>[] suppliers) {
        super(label, description, owner, value, valueChangeListeners, suppliers);
    }

    public CheckBoxValue(String label, String description, Object owner, Boolean value, Supplier<Boolean>[] suppliers) {
        super(label, description, owner, value, null, suppliers);
    }

    public CheckBoxValue(String label, String description, Object owner, Boolean value, ValueChangeListener[] valueChangeListeners) {
        super(label, description, owner, value, valueChangeListeners, null);
    }

    public CheckBoxValue(String label, String description, Object owner, Boolean value) {
        super(label, description, owner, value);
    }

    @Override
    public String getValueAsString() {
        return getValue().toString();
    }

    @Override
    public void setValue(String input) {
        Optional<Boolean> result = BooleanParser.parse(input);
        result.ifPresent(aBoolean -> this.value = aBoolean);
    }

    public void toggle() {
        this.value ^= true;
    }

    public boolean isEnabled() {
        return this.value;
    }

    public void setEnabled(boolean enabled) {
        this.value = enabled;
    }
}