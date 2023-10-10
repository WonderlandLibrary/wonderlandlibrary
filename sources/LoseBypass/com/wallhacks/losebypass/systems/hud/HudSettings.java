/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.hud;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import java.awt.Color;
import java.util.Arrays;

public class HudSettings
extends SettingsHolder {
    public IntSetting fontSize = new IntSetting("FontSize", this, 14, 10, 100).visibility(v -> false);
    public ColorSetting textColor = new ColorSetting("TextColor", this, new Color(44, 92, 118, 255));
    public ModeSetting backgroundMode = new ModeSetting("Mode", this, "Normal", Arrays.asList("Normal", "Off", "Blur", "Outline"));
    public ColorSetting background = new ColorSetting("BackGround", this, new Color(0x27000000, true));

    @Override
    public String getName() {
        return "Hud";
    }
}

