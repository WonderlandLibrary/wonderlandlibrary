package dev.monsoon.module.setting.impl;

import dev.monsoon.module.setting.Setting;

public class TextSetting extends Setting {

    public String value;

    public TextSetting(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
