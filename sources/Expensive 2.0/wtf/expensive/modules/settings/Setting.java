package wtf.expensive.modules.settings;

import lombok.Getter;

import java.awt.*;
import java.util.function.Supplier;

@Getter
public abstract class Setting {
    private final String name;
    public Supplier<Boolean> visible = () -> true;
    public Color color = Color.WHITE;
    public abstract SettingType getType();

    public Setting(String name) {
        this.name = name;
    }


    public Boolean visible() {
        return visible.get();
    }

    public enum SettingType {
        BOOLEAN_OPTION,
        SLIDER_SETTING,
        MODE_SETTING,
        COLOR_SETTING,
        MULTI_BOX_SETTING,
        BIND_SETTING,
        BUTTON_SETTING,
        TEXT_SETTING
    }
}