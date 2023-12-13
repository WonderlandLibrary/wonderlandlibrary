/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import tk.rektsky.Client;
import tk.rektsky.module.settings.Setting;

public class ListSetting
extends Setting {
    private String[] values;
    private String value;
    private String defaultValue;
    private ListSettingChangedHandler onChange = (oldValue, newValue) -> {};

    public int getCounter() {
        int i2 = 0;
        int ret = 0;
        for (String v2 : this.values) {
            if (v2.equals(this.value)) {
                ret = i2;
            }
            ++i2;
        }
        return ret;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            for (String v2 : this.values) {
                if (!v2.equalsIgnoreCase((String)value)) continue;
                this.onChange.onChange(this.value, v2);
                this.value = v2;
                return;
            }
            Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Value!");
        }
    }

    public ListSetting(String name, String[] values, String defaultValue) {
        this.name = name;
        this.values = values;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public ListSetting setOnChange(ListSettingChangedHandler onChange) {
        this.onChange = onChange;
        return this;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String[] getValues() {
        return this.values;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public static interface ListSettingChangedHandler {
        public void onChange(String var1, String var2);
    }
}

