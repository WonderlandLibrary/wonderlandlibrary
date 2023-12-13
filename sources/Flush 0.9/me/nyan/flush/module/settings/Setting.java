package me.nyan.flush.module.settings;

import me.nyan.flush.module.Module;

import java.util.function.BooleanSupplier;

public class Setting {
    private final String name;
    private final Module module;
    private final BooleanSupplier supplier;

    public Setting(String name, Module module) {
        this(name, module, null);
    }

    public Setting(String name, Module module, BooleanSupplier supplier) {
        this.name = name;
        this.module = module;
        this.supplier = supplier == null ? () -> true : supplier;
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    protected void register() {
        module.addSetting(this);
    }

    public boolean shouldShow() {
        return supplier.getAsBoolean();
    }
}