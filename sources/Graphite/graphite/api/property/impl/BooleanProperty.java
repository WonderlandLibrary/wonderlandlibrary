package graphite.api.property.impl;

import graphite.api.property.Property;

public final class BooleanProperty extends Property<Boolean> {
    public BooleanProperty(final String name, final boolean value) {
        super(name, value, null);
    }

    public BooleanProperty(final String name, final boolean value, final Dependency dependency) {
        super(name, value, dependency);
    }
}