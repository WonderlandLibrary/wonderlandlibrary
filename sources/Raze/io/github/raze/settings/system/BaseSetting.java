package io.github.raze.settings.system;

import io.github.raze.modules.system.AbstractModule;

import java.util.function.BooleanSupplier;

public class BaseSetting {

    public String name;
    public AbstractModule parent;

    private BooleanSupplier hidden = () -> false;

    public String getName() {
        return name;
    }

    public AbstractModule getParent() {
        return parent;
    }

    public boolean isHidden() {
        return hidden.getAsBoolean();
    }

    public <T extends BaseSetting> T setHidden(BooleanSupplier hidden) {
        this.hidden = hidden;
        return (T) this;
    }
}
