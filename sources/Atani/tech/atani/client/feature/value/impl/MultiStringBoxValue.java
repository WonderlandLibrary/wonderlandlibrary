package tech.atani.client.feature.value.impl;

import com.google.common.base.Supplier;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.interfaces.ValueChangeListener;

import java.util.*;
import java.util.stream.Collectors;

public class MultiStringBoxValue extends Value<List<String>> {

    String[] values;

    public MultiStringBoxValue(String name, String description, Object owner, String[] enabled, String values[], ValueChangeListener[] valueChangeListeners, Supplier<Boolean>[] suppliers) {
        super(name, description, owner, new ArrayList<>(Arrays.asList(enabled)), valueChangeListeners, suppliers);
        this.values = values;
    }

    public MultiStringBoxValue(String name, String description, Object owner, String[] enabled, String values[], Supplier<Boolean>[] suppliers) {
        super(name, description, owner, new ArrayList<>(Arrays.asList(enabled)), null, suppliers);
        this.values = values;
    }

    public MultiStringBoxValue(String name, String description, Object owner, String[] enabled, String values[], ValueChangeListener[] valueChangeListeners) {
        super(name, description, owner, new ArrayList<>(Arrays.asList(enabled)), valueChangeListeners, null);
        this.values = values;
    }

    public MultiStringBoxValue(String name, String description, Object owner, String[] enabled, String values[]) {
        super(name, description, owner, new ArrayList<>(Arrays.asList(enabled)));
        this.values = values;
    }

    public void toggle(String input) {
        this.set(input, !this.get(input));
    }

    public void set(String input, boolean state) {
        if(state) {
            if(!this.getValue().contains(input))
                this.getValue().add(input);
        } else {
            if(this.getValue().contains(input))
                this.getValue().remove(input);
        }
    }

    public String[] getValues() {
        return values;
    }

    public boolean get(String string) {
        return this.getValue().contains(string);
    }

    @Override
    public String getValueAsString() {
        return getValue().stream().collect(Collectors.joining(","));
    }

    @Override
    public void setValue(String string) {
        this.setValue(new ArrayList<>(Arrays.asList(string.split(","))));
    }

}
