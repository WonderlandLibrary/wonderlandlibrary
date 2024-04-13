package com.canon.majik.impl.setting.settings;

import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting<Color> {
    public ColorSetting(String name, Color value, Module parent) {
        super(name, value, parent);
    }
}
