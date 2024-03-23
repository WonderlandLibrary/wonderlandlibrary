/*
WalmartSolutions on top
 */
package dev.zprestige.prestige.client.ui.drawables.gui.buttons;

import dev.zprestige.prestige.client.managers.SettingManager;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.setting.impl.ColorSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import java.awt.Color;

public class MenuButton extends SettingManager {
    public ModeSetting mode;
    public BindSetting bind = this.setting("Menu Key", 344);
    public ColorSetting color;

    public MenuButton() {
        this.mode = setting("Descriptions", "Mouse", new String[]{"Mouse", "Search Bar"});
        this.color = setting("Menu Color", new Color(131, 118, 252));
    }

    public ModeSetting getMode() {
        return mode;
    }

    public BindSetting getBind() {
        return bind;
    }

    public ColorSetting getColor() {
        return color;
    }
}
