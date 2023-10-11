package io.github.raze.settings.collection;

import io.github.raze.modules.system.AbstractModule;
import io.github.raze.settings.system.BaseSetting;

public class BooleanSetting extends BaseSetting {

    public boolean value;

    public BooleanSetting(AbstractModule parent, String name, boolean value) {
        this.parent = parent;
        this.name = name;
        this.value = value;
    }

    public boolean toggle() {
        this.value = !this.value;

        return this.value;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        onChange(this.value, value);
        change(value);
    }

    public void change(boolean value) {
        this.value = value;
    }

    public void onChange(boolean old, boolean current) { /* */ }

}
