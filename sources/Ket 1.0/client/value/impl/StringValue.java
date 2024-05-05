package client.value.impl;

import client.value.Mode;
import client.value.Value;
import client.module.Module;

import java.util.List;
import java.util.function.BooleanSupplier;

public class StringValue extends Value<String> {

    public StringValue(final String name, final Module parent, final String defaultValue) {
        super(name, parent, defaultValue);
    }

    public StringValue(final String name, final Mode<?> parent, final String defaultValue) {
        super(name, parent, defaultValue);
    }

    public StringValue(final String name, final Module parent, final String defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    public StringValue(final String name, final Mode<?> parent, final String defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    @Override
    public List<Value<?>> getSubValues() {
        return null;
    }
}