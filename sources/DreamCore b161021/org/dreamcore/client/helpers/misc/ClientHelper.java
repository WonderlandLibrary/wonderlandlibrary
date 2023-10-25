package org.dreamcore.client.helpers.misc;

import net.minecraft.client.multiplayer.ServerData;
import org.dreamcore.client.feature.impl.hud.FeatureList;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.ui.font.MCFontRenderer;

import java.awt.*;

public class ClientHelper implements Helper {

    public static ServerData serverData;

    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColorValue());
        Color twoColor = new Color(HUD.twocolor.getColorValue());
        double time = HUD.time.getNumberValue();
        String mode = HUD.colorList.getOptions();
        float yDist = (float) 4;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int) (yDist * 200 * time), FeatureList.rainbowSaturation.getNumberValue(), FeatureList.rainbowBright.getNumberValue());
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int) yDist);
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().darker().getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twoColor.getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    public static MCFontRenderer getFontRender() {
        MCFontRenderer font = mc.fontRenderer;
        String mode = HUD.font.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = mc.sfuiFontRender;
        } else if (mode.equalsIgnoreCase("SF UI")) {
            font = mc.fontRenderer;
        } else if (mode.equalsIgnoreCase("Verdana")) {
            font = mc.verdanaFontRender;
        } else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = mc.robotoRegularFontRender;
        } else if (mode.equalsIgnoreCase("Lato")) {
            font = mc.latoFontRender;
        } else if (mode.equalsIgnoreCase("Open Sans")) {
            font = mc.openSansFontRender;
        } else if (mode.equalsIgnoreCase("Ubuntu")) {
            font = mc.ubuntuFontRender;
        } else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = mc.lucidaConsoleFontRenderer;
        } else if (mode.equalsIgnoreCase("Calibri")) {
            font = mc.calibri;
        } else if (mode.equalsIgnoreCase("Product Sans")) {
            font = mc.productsans;
        } else if (mode.equalsIgnoreCase("RaleWay")) {
            font = mc.raleway;
        } else if (mode.equalsIgnoreCase("Kollektif")) {
            font = mc.kollektif;
        } else if (mode.equalsIgnoreCase("CircleRegular")) {
            font = mc.circleregular;
        } else if (mode.equalsIgnoreCase("MontserratRegular")) {
            font = mc.montserratRegular;
        } else if (mode.equalsIgnoreCase("MontserratLight")) {
            font = mc.montserratLight;
        } else if (mode.equalsIgnoreCase("Menlo")) {
            font = mc.menlo;
        }
        return font;
    }
}
