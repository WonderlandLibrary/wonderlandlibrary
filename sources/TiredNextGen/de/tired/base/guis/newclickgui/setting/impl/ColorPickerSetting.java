package de.tired.base.guis.newclickgui.setting.impl;

import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.util.render.ColorPicker;
import de.tired.base.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Supplier;

@Getter
@Setter
public class ColorPickerSetting extends Setting {

    private String value;
    public Color ColorPickerC;

    public ColorPicker colorPicker = new ColorPicker(this);

    public ColorPickerSetting(String name, Module parent, Color color) {
        super(name, parent, () -> true);
        this.ColorPickerC = color;
    }

    public ColorPickerSetting(String name, Module parent, Color color, Supplier<Boolean> visible) {
        super(name, parent, visible);
        this.ColorPickerC = color;
    }
}