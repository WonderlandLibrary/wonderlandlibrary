/*
 * Decompiled with CFR 0.150.
 */
package markgg.settings;

import markgg.settings.Setting;

public class BooleanSetting
extends Setting {
    public boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BooleanSetting(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}

