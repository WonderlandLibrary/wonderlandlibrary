package wtf.monsoon.api.setting.impl;

import wtf.monsoon.api.setting.Setting;

public class TextSetting extends Setting {

    public String value;

    public TextSetting(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
