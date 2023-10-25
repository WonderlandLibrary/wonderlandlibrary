package ru.smertnix.celestial.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import ru.smertnix.celestial.feature.impl.hud.FeatureList;
import ru.smertnix.celestial.feature.impl.visual.ESP;
import ru.smertnix.celestial.ui.font.MCFontRenderer;
import ru.smertnix.celestial.utils.Helper;

import java.awt.*;

public class ClientHelper implements Helper {
    public static ServerData serverData;

    public static Color getClientColor() {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 10;
        String mode = FeatureList.backGroundColorMode.getCurrentMode();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        }
        return color;
    }

    public static Color getESPColor() {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 10;
        String mode = FeatureList.backGroundColorMode.getCurrentMode();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        }
        return color;
    }
    public static Color getHealthColor() {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 10;
        String mode = FeatureList.backGroundColorMode.getCurrentMode();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        }
        return color;
    }
    public static Color getTargetHudColor() {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 10;
        String mode = FeatureList.backGroundColorMode.getCurrentMode();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        }
        return color;
    }
    
    public static Color getClientColor(float yStep, float yStepFull, int speed) {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 11;
        String mode = FeatureList.backGroundColorMode.getOptions();
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbowCol(1, 1, 0.5f, speed), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep / 10) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
        	if (speed == 5) {
        		color = ColorUtils.astolfo(yStep / 3.7f - yStep / 3.7f + 1, yStepFull, 0.7f, speed * 2);
        	} else {
        		color = ColorUtils.astolfo(yStep, yStepFull, 0.7f, speed);
        	}
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep / 10) / 60);
        }
        return color;
    }

    public static Color getClientColor(float yStep, float astolfoastep, float yStepFull, int speed) {
        Color color = new Color(FeatureList.backGroundColor.getColorValue());
        Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        double time = 11;
        String mode = FeatureList.backGroundColorMode.getOptions();
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().mntsb_18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            color = ColorUtils.TwoColoreffect(ColorUtils.rainbowCol(yStep, yStepFull, 0.5f, speed), new Color(color.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (60 * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo(astolfoastep, yStepFull, 1, speed);
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(FeatureList.backGroundColor.getColorValue());
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep * 2.55) / 60);
        }
        return color;
    }

    public static MCFontRenderer getFontRender() {
        Minecraft mc = Minecraft.getMinecraft();
        MCFontRenderer font = mc.mntsb_15;
        return font;
    }
}