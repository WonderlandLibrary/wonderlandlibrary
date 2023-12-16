package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.function.Supplier;

public class TextSetting extends Setting {
    public String text;

    public TextSetting(String name, String text) {
        super(name);
       this.text = text;
    }

    public String get() {
        return text;
    }

    public TextSetting setVisible(Supplier<Boolean> bool) {
        visible = bool;
        return this;
    }

    @Override
    public SettingType getType() {
        return SettingType.TEXT_SETTING;
    }
}
