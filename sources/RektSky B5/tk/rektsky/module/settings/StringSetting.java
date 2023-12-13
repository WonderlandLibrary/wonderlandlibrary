/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.settings;

import tk.rektsky.module.settings.Setting;

public class StringSetting
extends Setting {
    private int maxTexts = -1;
    private int minTexts = -1;
    private String value;
    private String defaultValue;

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            if (this.maxTexts == -1) {
                this.maxTexts = 34;
            }
            if (((String)value).length() < this.minTexts || ((String)value).length() > this.maxTexts) {
                return;
            }
            this.value = (String)value;
        }
    }

    public StringSetting(String name, int minTexts, int maxTexts, String defaultValue) {
        this.name = name;
        this.minTexts = minTexts;
        this.maxTexts = maxTexts;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public int getMaxTexts() {
        return this.maxTexts;
    }

    public int getMinTexts() {
        return this.minTexts;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}

