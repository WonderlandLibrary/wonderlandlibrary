package wtf.dawn.settings.impl;

import wtf.dawn.settings.Setting;

public class KeybindSetting extends Setting {
    private int code;

    public KeybindSetting(int code) {
        this.name = "Keybind";
        this.code = code;
    }

    public int getCode() {
        return this.code == -1 ? 0 : this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Integer getConfigValue() {
        return this.getCode();
    }
}

