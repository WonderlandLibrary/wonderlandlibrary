/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.properties;

import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.PropertyManager;
import cow.milkgod.cheese.utils.Logger;
import java.util.ArrayList;

public class Property<Type> {
    private final String name;
    private Module module;
    public Type value;
    public Type defaultvalue;

    public Property(Module module, String name, Type value) {
        this.module = module;
        this.name = name;
        this.value = value;
        this.defaultvalue = value;
        PropertyManager.properties.add(this);
    }

    public Type getDefaultValue() {
        return this.defaultvalue;
    }

    public Type getValue() {
        return this.value;
    }

    public void setValue(Type t) {
        this.value = t;
    }

    public String getName() {
        return this.name;
    }

    public void reset() {
        this.value = this.defaultvalue;
        Logger.logSetMessage(this.getModule().getName(), this.getName(), this);
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module mod) {
        this.module = mod;
    }
}

