// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.module.impl.render.HUDMod;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import java.awt.Color;

public class GradientColorWheel
{
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private ModeSetting colorMode;
    private ColorSetting colorSetting;
    
    public GradientColorWheel() {
        this.color1 = Color.BLACK;
        this.color2 = Color.BLACK;
        this.color3 = Color.BLACK;
        this.color4 = Color.BLACK;
    }
    
    public ModeSetting createModeSetting(final String name, final String... extraModes) {
        final List<String> modesList = new ArrayList<String>();
        modesList.add("Sync");
        modesList.add("Custom");
        modesList.addAll(Arrays.asList(extraModes));
        this.colorMode = new ModeSetting(name, "Sync", (String[])modesList.toArray(new String[0]));
        (this.colorSetting = new ColorSetting("Custom Color", Color.PINK)).addParent(this.colorMode, modeSetting -> modeSetting.is("Custom"));
        return this.colorMode;
    }
    
    public void setColorsForMode(final String mode, final Color color) {
        this.setColorsForMode(mode, color, color, color, color);
    }
    
    public void setColorsForMode(final String mode, final Color color1, final Color color2, final Color color3, final Color color4) {
        if (this.colorMode.is(mode)) {
            this.color1 = color1;
            this.color2 = color2;
            this.color3 = color3;
            this.color4 = color4;
        }
    }
    
    public void setColors() {
        final int secondIndex = HUDMod.drawRadialGradients() ? 90 : 35;
        if (this.colorMode.is("Sync")) {
            if (HUDMod.isRainbowTheme()) {
                this.color1 = HUDMod.color1.getRainbow().getColor(0);
                this.color2 = HUDMod.color1.getRainbow().getColor(secondIndex);
                this.color3 = HUDMod.color1.getRainbow().getColor(180);
                this.color4 = HUDMod.color1.getRainbow().getColor(270);
            }
            else {
                this.setWheel(HUDMod.getClientColors());
            }
        }
        else if (this.colorMode.is("Custom")) {
            if (this.colorSetting.isRainbow()) {
                this.color1 = this.colorSetting.getRainbow().getColor(0);
                this.color2 = this.colorSetting.getRainbow().getColor(secondIndex);
                this.color3 = this.colorSetting.getRainbow().getColor(180);
                this.color4 = this.colorSetting.getRainbow().getColor(270);
            }
            else {
                this.setWheel(Pair.of(this.colorSetting.getColor(), this.colorSetting.getAltColor()));
            }
        }
        if (!HUDMod.drawRadialGradients()) {
            this.color4 = this.color1;
            this.color3 = this.color2;
        }
    }
    
    private void setWheel(final Pair<Color, Color> colors) {
        final int secondIndex = HUDMod.drawRadialGradients() ? 90 : 35;
        this.color1 = ColorUtil.interpolateColorsBackAndForth(15, 0, colors.getFirst(), colors.getSecond(), false);
        this.color2 = ColorUtil.interpolateColorsBackAndForth(15, secondIndex, colors.getFirst(), colors.getSecond(), false);
        this.color3 = ColorUtil.interpolateColorsBackAndForth(15, 180, colors.getFirst(), colors.getSecond(), false);
        this.color4 = ColorUtil.interpolateColorsBackAndForth(15, 270, colors.getFirst(), colors.getSecond(), false);
    }
    
    public Color getColor1() {
        return this.color1;
    }
    
    public Color getColor2() {
        return this.color2;
    }
    
    public Color getColor3() {
        return this.color3;
    }
    
    public Color getColor4() {
        return this.color4;
    }
    
    public ModeSetting getColorMode() {
        return this.colorMode;
    }
    
    public ColorSetting getColorSetting() {
        return this.colorSetting;
    }
}
