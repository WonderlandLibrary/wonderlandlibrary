package me.nyan.flush.module.settings;

import me.nyan.flush.module.Module;

import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

public class ModeSetting extends Setting {
    private String value;
    private final List<String> options;
    public boolean extended;

    public ModeSetting(String name, Module parent, String value, BooleanSupplier supplier, List<String> options) {
        super(name, parent, supplier);
        this.value = value;
        this.options = options;
        register();
    }

    public ModeSetting(String name, Module parent, String value, BooleanSupplier supplier, String... options) {
        this(name, parent, value, supplier, Arrays.asList(options));
    }

    public ModeSetting(String name, Module parent, String value, List<String> options) {
        this(name, parent, value, null, options);
    }

    public ModeSetting(String name, Module parent, String value, String... options) {
        this(name, parent, value, null, options);
    }

    public void setValue(String value) {
        this.value = options.stream().filter(option -> option.equalsIgnoreCase(value)).findFirst().orElse(this.value);
    }

    public void cycle() {
        if (options.indexOf(value) + 1 >= options.size()) {
            setValue(options.get(0));
            return;
        }
        setValue(options.get(options.indexOf(value) + 1));
    }

    public void cycleInverted() {
        if (options.indexOf(value) - 1 < 0) {
            setValue(options.get(options.size() - 1));
            return;
        }
        setValue(options.get(options.indexOf(value) - 1));
    }

    public String getValue() {
        return value;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean is(String value) {
        return this.value.equalsIgnoreCase(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
