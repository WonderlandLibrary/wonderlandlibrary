package club.dortware.client.property.impl;

import club.dortware.client.property.impl.interfaces.INameable;
import club.dortware.client.property.Property;

import java.util.Arrays;
import java.util.List;

public final class EnumProperty<T1> extends Property<T1, INameable> {
    private final List<INameable> values;

    public EnumProperty(String name, T1 owner, INameable... values) {
        super(name, owner, values[0]);
        this.values = Arrays.asList(values);
    }

    public List<INameable> getValues() {
        return values;
    }

    public INameable getCastedValue() {
        return super.getValue();
    }
}
