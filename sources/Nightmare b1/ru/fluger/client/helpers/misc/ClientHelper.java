// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.misc;

import ru.fluger.client.feature.impl.hud.ClientFont;
import ru.fluger.client.ui.font.MCFontRenderer;
import ru.fluger.client.feature.impl.hud.ArrayList;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.feature.impl.hud.TargetHUD;
import java.awt.Color;
import ru.fluger.client.helpers.Helper;

public class ClientHelper implements Helper
{
    public static bse serverData;
    
    public static Color getTargetHudColor() {
        Color color = Color.white;
        final Color onecolor = new Color(TargetHUD.targetHudColor.getColor());
        final String mode = TargetHUD.thudColorMode.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += getFontRender().getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow(20, 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(5000.0f, 1);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(onecolor.getRGB());
        }
        else if (mode.equalsIgnoreCase("Client")) {
            color = getClientColor();
        }
        return color;
    }
    
    public static Color getClientColor() {
        Color color = Color.white;
        final Color onecolor = new Color(HUD.onecolor.getColor());
        final Color twoColor = new Color(HUD.twocolor.getColor());
        final double time = HUD.time.getCurrentValue();
        final String mode = HUD.colorList.getOptions();
        final float yDist = 4.0f;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int)(yDist * 200.0f * time), HUD.rainbowSaturation.getCurrentValue(), HUD.rainbowBright.getCurrentValue());
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int)yDist);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = PaletteHelper.TwoColorEffect(onecolor, onecolor.darker().darker(), Math.abs(System.currentTimeMillis() / time) / time + 6.0 * (yDist * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        }
        else if (mode.equalsIgnoreCase("Pulse")) {
            color = PaletteHelper.TwoColorEffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0 * (yDist * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yDist * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }
    
    public static Color getClientColor(final float yStep, final float yStepFull, final int speed) {
        Color color = Color.white;
        final Color onecolor = new Color(ArrayList.oneColor.getColor());
        final Color twoColor = new Color(ArrayList.twoColor.getColor());
        final double time = 10.0;
        final String mode = ArrayList.colorList.getOptions();
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int)(yStep * time), 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(yStep, yStepFull, 0.5f, (float)speed);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(onecolor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        return color;
    }
    
    public static MCFontRenderer getFontRender() {
        MCFontRenderer font = ClientHelper.mc.fontRenderer;
        final String mode = ClientFont.font.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = ClientHelper.mc.comfortaa;
        }
        else if (mode.equalsIgnoreCase("SF UI")) {
            font = ClientHelper.mc.fontRenderer;
        }
        else if (mode.equalsIgnoreCase("Verdana")) {
            font = ClientHelper.mc.verdanaFontRender;
        }
        else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = ClientHelper.mc.robotoRegularFontRender;
        }
        else if (mode.equalsIgnoreCase("Lato")) {
            font = ClientHelper.mc.latoFontRender;
        }
        else if (mode.equalsIgnoreCase("Rubik")) {
            font = ClientHelper.mc.rubik_18;
        }
        else if (mode.equalsIgnoreCase("Open Sans")) {
            font = ClientHelper.mc.openSansFontRender;
        }
        else if (mode.equalsIgnoreCase("Ubuntu")) {
            if (ArrayList.fontSize.getCurrentValue() == 14.0f) {
                font = ClientHelper.mc.ubuntuFontRender14;
            }
            if (ArrayList.fontSize.getCurrentValue() == 15.0f) {
                font = ClientHelper.mc.ubuntuFontRender15;
            }
            if (ArrayList.fontSize.getCurrentValue() == 16.0f) {
                font = ClientHelper.mc.ubuntuFontRender16;
            }
            if (ArrayList.fontSize.getCurrentValue() == 17.0f) {
                font = ClientHelper.mc.ubuntuFontRender17;
            }
            if (ArrayList.fontSize.getCurrentValue() == 18.0f) {
                font = ClientHelper.mc.ubuntuFontRender18;
            }
            if (ArrayList.fontSize.getCurrentValue() == 19.0f) {
                font = ClientHelper.mc.ubuntuFontRender;
            }
        }
        else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = ClientHelper.mc.lucidaConsoleFontRenderer;
        }
        else if (mode.equalsIgnoreCase("Calibri")) {
            font = ClientHelper.mc.calibri;
        }
        else if (mode.equalsIgnoreCase("Product Sans")) {
            font = ClientHelper.mc.productsans;
        }
        else if (mode.equalsIgnoreCase("RaleWay")) {
            font = ClientHelper.mc.raleway;
        }
        else if (mode.equalsIgnoreCase("Kollektif")) {
            font = ClientHelper.mc.kollektif;
        }
        else if (mode.equalsIgnoreCase("Bebas Book")) {
            font = ClientHelper.mc.bebasRegular;
        }
        return font;
    }
}
