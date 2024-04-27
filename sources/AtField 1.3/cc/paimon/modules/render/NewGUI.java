/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package cc.paimon.modules.render;

import cc.paimon.skid.lbp.newVer.NewUi;
import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(name="NewGUI", description="next generation clickgui.", category=ModuleCategory.RENDER, canEnable=false)
public class NewGUI
extends Module {
    public double slide;
    public static final BoolValue fastRenderValue = new BoolValue("FastRender", false);
    private static final IntegerValue colorRedValue;
    private static final IntegerValue mixerSecondsValue;
    public double progress = 0.0;
    private static final IntegerValue colorBlueValue;
    private static final FloatValue brightnessValue;
    private static final FloatValue saturationValue;
    private static final IntegerValue colorGreenValue;
    private static final ListValue colorModeValue;

    public static Color getAccentColor() {
        Color color = new Color(255, 255, 255, 255);
        switch (((String)colorModeValue.get()).toLowerCase()) {
            case "custom": {
                color = new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get());
                break;
            }
            case "rainbow": {
                color = new Color(RenderUtils.getRainbowOpaque((Integer)mixerSecondsValue.get(), ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue(), 0));
                break;
            }
            case "sky": {
                color = RenderUtils.skyRainbow(0, ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue());
                break;
            }
            case "liquidslowly": {
                color = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)saturationValue.get()).floatValue(), ((Float)brightnessValue.get()).floatValue());
                break;
            }
            case "fade": {
                color = ColorUtils.fade(new Color((Integer)colorRedValue.get(), (Integer)colorGreenValue.get(), (Integer)colorBlueValue.get()), 0, 100);
            }
        }
        return color;
    }

    @Override
    public void onEnable() {
        this.progress = 0.0;
        this.slide = 0.0;
        mc2.func_147108_a((GuiScreen)NewUi.getInstance());
    }

    static {
        colorModeValue = new ListValue("Color", new String[]{"Custom", "Sky", "Rainbow", "LiquidSlowly", "Fade"}, "Custom");
        colorRedValue = new IntegerValue("Red", 0, 0, 255);
        colorGreenValue = new IntegerValue("Green", 140, 0, 255);
        colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
        brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
    }
}

