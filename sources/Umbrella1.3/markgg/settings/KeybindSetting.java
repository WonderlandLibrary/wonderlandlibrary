/*
 * Decompiled with CFR 0.150.
 */
package markgg.settings;

import markgg.settings.Setting;

public class KeybindSetting
extends Setting {
    public int code;

    public KeybindSetting(int code) {
        this.name = "Keybind";
        this.code = code;
    }

    public int getKeyCode() {
        return this.code;
    }

    public void setKeyCode(int code) {
        this.code = code;
    }
}

