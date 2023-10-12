package wtf.diablo.settings.impl;

import wtf.diablo.settings.Setting;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean defaultValue){
        this.value = defaultValue;
        this.name = name;
    }
    public Object getObjectValue() {
        return value;
    }
    public boolean getValue() { return value; }

    public void setValue(boolean value) {
        this.value = value;
    }
}
