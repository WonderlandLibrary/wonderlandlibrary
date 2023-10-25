/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.misc;

import java.awt.Color;
import net.minecraft.client.multiplayer.ServerData;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.ui.font.MCFontRenderer;

public class ClientHelper
implements Helper {
    public static ServerData serverData;

    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColor());
        Color twoColor = new Color(HUD.twocolor.getColor());
        double time = HUD.time.getCurrentValue();
        String mode = HUD.colorList.getOptions();
        float yDist = 4.0f;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int)((double)(yDist * 200.0f) * time), HUD.rainbowSaturation.getCurrentValue(), HUD.rainbowBright.getCurrentValue());
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int)yDist);
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = PaletteHelper.TwoColorEffect(onecolor, onecolor.darker().darker(), Math.abs((double)System.currentTimeMillis() / time) / time + 6.0 * ((double)yDist * 2.55) / 60.0);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        } else if (mode.equalsIgnoreCase("Pulse")) {
            color = PaletteHelper.TwoColorEffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs((double)System.currentTimeMillis() / time) / 100.0 + 6.0 * ((double)yDist * 2.55) / 60.0);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs((double)System.currentTimeMillis() / time) / 100.0 + 3.0 * ((double)yDist * 2.55) / 60.0);
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    public static MCFontRenderer getFontRender() {
        MCFontRenderer font = ClientHelper.mc.fontRenderer;
        String mode = ClientFont.font.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = ClientHelper.mc.comfortaa;
        } else if (mode.equalsIgnoreCase("SF UI")) {
            font = ClientHelper.mc.fontRenderer;
        } else if (mode.equalsIgnoreCase("Verdana")) {
            font = ClientHelper.mc.verdanaFontRender;
        } else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = ClientHelper.mc.robotoRegularFontRender;
        } else if (mode.equalsIgnoreCase("Lato")) {
            font = ClientHelper.mc.latoFontRender;
        } else if (mode.equalsIgnoreCase("Open Sans")) {
            font = ClientHelper.mc.openSansFontRender;
        } else if (mode.equalsIgnoreCase("Ubuntu")) {
            font = ClientHelper.mc.ubuntuFontRender;
        } else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = ClientHelper.mc.lucidaConsoleFontRenderer;
        } else if (mode.equalsIgnoreCase("Calibri")) {
            font = ClientHelper.mc.calibri;
        } else if (mode.equalsIgnoreCase("Product Sans")) {
            font = ClientHelper.mc.productsans;
        } else if (mode.equalsIgnoreCase("RaleWay")) {
            font = ClientHelper.mc.raleway;
        } else if (mode.equalsIgnoreCase("Kollektif")) {
            font = ClientHelper.mc.kollektif;
        } else if (mode.equalsIgnoreCase("Bebas Book")) {
            font = ClientHelper.mc.bebasRegular;
        }
        return font;
    }
}

