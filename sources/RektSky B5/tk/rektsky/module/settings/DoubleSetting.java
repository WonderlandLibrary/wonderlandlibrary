/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.module.settings.Setting;

public class DoubleSetting
extends Setting {
    private Double max;
    private Double min;
    private Double value;
    private Double defaultValue;

    @Override
    public void setValue(Object value) {
        if (value instanceof Double) {
            if ((Double)value < this.min || (Double)value > this.max) {
                Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Value!");
                return;
            }
            this.value = (Double)value;
        }
    }

    public DoubleSetting(String name, Double min, Double max, Double defaultValue) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    public Double getMax() {
        return this.max;
    }

    public Double getMin() {
        return this.min;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}

