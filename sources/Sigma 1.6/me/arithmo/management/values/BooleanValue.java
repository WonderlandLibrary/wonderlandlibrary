/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.values;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import me.arithmo.management.values.Value;
import me.arithmo.module.Module;

public class BooleanValue
extends Value<Boolean> {
    private boolean isModType;

    public BooleanValue(String name, boolean value, Module module, boolean isModType) {
        super(name, value, module);
        this.isModType = isModType;
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value);
        for (Field field : this.getModule().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Value.Val.class) || !field.getName().equalsIgnoreCase(this.getValueName())) continue;
            try {
                if (!field.getType().isAssignableFrom(Boolean.TYPE)) continue;
                field.setBoolean(this.getModule(), value);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setValueHard(Boolean value) {
        super.setValue(value);
        for (Field field : this.getModule().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Value.Val.class) || !field.getName().equalsIgnoreCase(this.getValueName())) continue;
            try {
                if (!field.getType().isAssignableFrom(Boolean.TYPE)) continue;
                field.setBoolean(this.getModule(), value);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void toggle() {
        this.setValue((Boolean)this.getValue() == false);
    }

    public boolean isModType() {
        return this.isModType;
    }
}

