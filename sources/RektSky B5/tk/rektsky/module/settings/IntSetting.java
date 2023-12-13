/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.module.settings.Setting;

public class IntSetting
extends Setting {
    private int max;
    private int min;
    private int value;
    private int defaultValue;

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            if ((Integer)value < this.min || (Integer)value > this.max) {
                Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Value!");
                return;
            }
            this.value = (Integer)value;
        }
    }

    public IntSetting(String name, int min, int max, int defaultValue) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}

