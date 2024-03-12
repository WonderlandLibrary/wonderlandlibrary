// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.module.settings.Setting;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class Glint extends Module
{
    public final ModeSetting colorMode;
    public final ColorSetting color;
    
    public Glint() {
        super("Glint", "Glint", Category.RENDER, "Colors the enchantment glint");
        this.colorMode = new ModeSetting("Color Mode", "Sync", new String[] { "Sync", "Custom" });
        (this.color = new ColorSetting("Color", Color.PINK)).addParent(this.colorMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(this.colorMode, this.color);
    }
    
    public Color getColor() {
        Color customColor = Color.WHITE;
        final String mode = this.colorMode.getMode();
        switch (mode) {
            case "Sync": {
                final Pair<Color, Color> colors = HUDMod.getClientColors();
                if (HUDMod.isRainbowTheme()) {
                    customColor = colors.getFirst();
                    break;
                }
                customColor = ColorUtil.interpolateColorsBackAndForth(20, 1, colors.getFirst(), colors.getSecond(), false);
                break;
            }
            case "Custom": {
                customColor = this.color.getColor();
                break;
            }
        }
        return customColor;
    }
}
