/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.module.settings.Setting;

public class BooleanSetting
extends Setting {
    private boolean value;
    private boolean defaultValue;

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            if (((String)value).equalsIgnoreCase("true")) {
                this.value = true;
            } else if (((String)value).equalsIgnoreCase("false")) {
                this.value = false;
            } else {
                Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Value!");
            }
        }
        if (value instanceof Boolean) {
            this.value = (Boolean)value;
        }
    }

    public BooleanSetting(String name, Boolean defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}

