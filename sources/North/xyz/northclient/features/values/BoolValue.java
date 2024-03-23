package xyz.northclient.features.values;

import lombok.Getter;
import lombok.Setter;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Value;
import xyz.northclient.features.VisibleSupplier;

public class BoolValue extends Value<Boolean> {
    private String name;
    private boolean value = true;

    @Getter
    private AbstractModule owner;

    private VisibleSupplier visibleSupplier;

    public BoolValue(String name, AbstractModule owner) {
        this.name = name;
        this.owner = owner;
    }

    public BoolValue(String name, AbstractModule owner, VisibleSupplier visibleSupplier) {
        this.name = name;
        this.owner = owner;
        this.visibleSupplier = visibleSupplier;
    }

    public BoolValue setDefault(Boolean v) {
         set(v);
         return this;
    }
    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public VisibleSupplier isVisible() {
        return this.visibleSupplier == null ? () -> true : this.visibleSupplier;
    }
}
