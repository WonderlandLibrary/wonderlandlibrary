package me.nyan.flush.module.settings;

import me.nyan.flush.module.Module;

import java.util.function.BooleanSupplier;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, Module parent, boolean value, BooleanSupplier supplier) {
        super(name, parent, supplier);
        this.value = value;
        register();
    }

    public BooleanSetting(String name, Module parent, boolean value) {
        this(name, parent, value, null);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
