/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.clientsetting.clientsettings;

import com.wallhacks.losebypass.systems.clientsetting.ClientSetting;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.KeySetting;
import com.wallhacks.losebypass.systems.setting.settings.StringSetting;
import java.awt.Color;

@ClientSetting.Registration(name="ClickGui", description="Config for basic client settings")
public class ClickGuiConfig
extends ClientSetting {
    StringSetting Prefix = new StringSetting("Prefix", this, ".");
    KeySetting bind = new KeySetting("ClickGui", this, 54);
    ColorSetting mainColor = new ColorSetting("ThemeColor", this, new Color(44, 92, 118, 255));
    BooleanSetting description = new BooleanSetting("Descriptions", this, true);
    BooleanSetting RenderCustomFont = new BooleanSetting("Custom", this, true);
    BooleanSetting RenderShadow = new BooleanSetting("FontShadow", this, false);
    public static int spacing = 4;
    public static ClickGuiConfig INSTANCE;

    public boolean getCustomFontEnabled() {
        return this.RenderCustomFont.isOn();
    }

    public boolean getFontShadow() {
        return this.RenderShadow.isOn();
    }

    public boolean description() {
        return this.description.isOn();
    }

    public int getBind() {
        return this.bind.getKey();
    }

    public Color getMainColor() {
        return this.mainColor.getColor();
    }

    public String getChatPrefix() {
        return (String)this.Prefix.getValue();
    }

    public ClickGuiConfig() {
        INSTANCE = this;
    }

    public static ClickGuiConfig getInstance() {
        return INSTANCE;
    }
}

