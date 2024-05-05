package io.github.raze.modules.collection.client;

import io.github.raze.Raze;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.modules.system.ingame.ModuleIngame;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.math.MathUtil;
import io.github.raze.utilities.collection.visual.ColorUtil;
import io.github.raze.utilities.collection.visual.RenderUtil;
import io.github.raze.utilities.collection.visual.RoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Watermark extends ModuleIngame {

    private final ArraySetting style, fontColor, fontCase, clockFormat;
    private final NumberSetting textRed, textGreen, textBlue, textAlpha;
    private final BooleanSetting bps, fps, clock;

    public Watermark() {
        super("Watermark", "Displays a watermark.", ModuleCategory.CLIENT);

        this.x = 5;
        this.y = 5;

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                style = new ArraySetting(this, "Style", "Jello", "Jello", "Classic"),
                fontColor = new ArraySetting(this, "Font Color", "Custom", "Custom", "Rainbow", "Astolfo"),
                fontCase = new ArraySetting(this, "Font Case", "Normal", "Normal", "Lower", "Upper"),

                textRed = new NumberSetting(this, "Text Red", 0, 255, 255)
                        .setHidden(() -> !fontColor.compare("Custom")),

                textGreen = new NumberSetting(this, "Text Green", 0, 255, 255)
                        .setHidden(() -> !fontColor.compare("Custom")),

                textBlue = new NumberSetting(this, "Text Blue", 0, 255, 255)
                        .setHidden(() -> !fontColor.compare("Custom")),

                textAlpha = new NumberSetting(this, "Text Alpha", 0, 255, 185)
                        .setHidden(() -> !fontColor.compare("Custom")),

                bps = new BooleanSetting(this,"Blocks per second", true),
                fps = new BooleanSetting(this,"Frames per second", true),
                clock = new BooleanSetting(this, "Clock", false),

                clockFormat = new ArraySetting(this, "Clock Display Format", "24-hours", "24-hours", "12-hours")
                        .setHidden(() -> !clock.get())

        );

        setEnabled(true);
    }

    @Override
    public void renderIngame() {
        ColorUtil.Theme theme = Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme();

        ScaledResolution sr = new ScaledResolution(mc);

        float squareMotion = (float)(MathUtil.square(mc.thePlayer.motionX) + MathUtil.square(mc.thePlayer.motionZ));
        float bpsValue = (float)MathUtil.round(Math.sqrt(squareMotion) * 20.0D * mc.timer.timerSpeed, (int) 2.0D);

        String timeStamp;
        if (clockFormat.get().equals("24-hours")) {
            timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        } else {
            timeStamp = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());
            if (fontCase.get().equals("Lower")) {
                timeStamp = timeStamp.toLowerCase(Locale.ROOT);
            } else if (fontCase.get().equals("Upper")) {
                timeStamp = timeStamp.toUpperCase(Locale.ROOT);
            }
        }

        int clockRectWidth = 50;
        int clockRectHeight = 25;

        int rectX = sr.getScaledWidth() - clockRectWidth - 5;
        int rectY = sr.getScaledHeight() - clockRectHeight - 5;

        float textRed, textGreen, textBlue, textAlpha;
        switch (fontColor.get()) {
            case "Custom":
                textRed = this.textRed.get().floatValue() / 255.0f;
                textGreen = this.textGreen.get().floatValue() / 255.0f;
                textBlue = this.textBlue.get().floatValue() / 255.0f;
                textAlpha = this.textAlpha.get().floatValue() / 255.0f;
                break;

            case "Rainbow":
                Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f, 2000);
                textRed = rainbowColor.getRed() / 255.0f;
                textGreen = rainbowColor.getGreen() / 255.0f;
                textBlue = rainbowColor.getBlue() / 255.0f;
                textAlpha = 1.0f;
                break;

            default:
                int yOffset = 1000;
                int yTotal = 2000;
                int astolfoRGB = ColorUtil.AstolfoRGB(yOffset, yTotal);
                textRed = ((astolfoRGB >> 16) & 0xFF) / 255.0f;
                textGreen = ((astolfoRGB >> 8) & 0xFF) / 255.0f;
                textBlue = (astolfoRGB & 0xFF) / 255.0f;
                textAlpha = 1.0f;
                break;
        }

        String watermarkText = Raze.INSTANCE.getName();
        String themeName = theme.getName();

        switch (fontCase.get()) {
            case "Normal":
                break;

            case "Lower":
                watermarkText = watermarkText.toLowerCase(Locale.ROOT);
                themeName = themeName.toLowerCase(Locale.ROOT);
                break;

            case "Upper":
                watermarkText = watermarkText.toUpperCase(Locale.ROOT);
                themeName = themeName.toUpperCase(Locale.ROOT);
                break;
        }

        String bpsText = "BPS: " + bpsValue;
        String fpsText = "FPS: " + Minecraft.getDebugFPS();

        if (fontCase.get().equals("Lower")) {
            bpsText = bpsText.toLowerCase(Locale.ROOT);
            fpsText = fpsText.toLowerCase(Locale.ROOT);
        } else if (fontCase.get().equals("Upper")) {
            bpsText = bpsText.toUpperCase(Locale.ROOT);
            fpsText = fpsText.toUpperCase(Locale.ROOT);
        }

        switch (style.get()) {
            case "Jello":
                CFontUtil.Jello_Regular_40.getRenderer().drawString(watermarkText, getX(), getY(), new Color(textRed, textGreen, textBlue, textAlpha));
                CFontUtil.Jello_Medium_24.getRenderer().drawString(themeName, getX(), getY() + CFontUtil.Jello_Light_40.getRenderer().getHeight() - 2, new Color(textRed, textGreen, textBlue, textAlpha));

                int yPos = (int) (sr.getScaledHeight() - (CFontUtil.Jello_Light_20.getRenderer().getHeight() + 7) - 5);

                if (bps.get()) {
                    CFontUtil.Jello_Light_20.getRenderer().drawString(bpsText, getX(), yPos, new Color(textRed, textGreen, textBlue, textAlpha));
                    yPos -= CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2;
                }

                if (fps.get()) {
                    CFontUtil.Jello_Light_20.getRenderer().drawString(fpsText, getX(), yPos, new Color(textRed, textGreen, textBlue, textAlpha));
                }

                if (clock.get()) {
                    int textWidth = (int) CFontUtil.Jello_Light_20.getRenderer().getStringWidth(timeStamp);
                    int textHeight = (int) CFontUtil.Jello_Light_20.getRenderer().getHeight();
                    RoundUtil.drawSmoothRoundedRect(rectX, rectY, rectX + clockRectWidth, rectY + clockRectHeight, 14, Color.WHITE.getRGB());
                    int textX = rectX + (clockRectWidth - textWidth) / 2;
                    int textY = rectY + (clockRectHeight - textHeight) / 2;
                    CFontUtil.Jello_Light_20.getRenderer().drawString(timeStamp, textX, textY, Color.BLACK);
                }
                break;

            case "Classic":
                String text = watermarkText + " v" + Raze.INSTANCE.getVersion() + " - " + mc.getSession().getUsername();

                RenderUtil.rectangle(5,5,CFontUtil.Jello_Light_20.getRenderer().getStringWidth(text) + 7.5,15,true, new Color(0,0,0,180));
                RenderUtil.rectangle(5,5,CFontUtil.Jello_Light_20.getRenderer().getStringWidth(text) + 7.5,15,false, new Color(textRed,textGreen,textBlue,textAlpha));
                CFontUtil.Jello_Light_20.getRenderer().drawString(text, 7.5, 7.5, new Color(textRed, textGreen, textBlue, textAlpha));

                int yPos2 = (int) (sr.getScaledHeight() - (CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2 + 7) - 5);

                if (bps.get()) {
                    int bpsTextWidth = (int) (CFontUtil.Jello_Light_20.getRenderer().getStringWidth(bpsText) + 7);
                    RenderUtil.rectangle(5, yPos2, bpsTextWidth, CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2, true, new Color(0, 0, 0, 180));
                    RenderUtil.rectangle(5, yPos2, bpsTextWidth, CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2, false, new Color(textRed, textGreen, textBlue, textAlpha));
                    CFontUtil.Jello_Light_20.getRenderer().drawString(bpsText, 7.5, yPos2 + 1, new Color(textRed, textGreen, textBlue, textAlpha));
                    yPos2 -= CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2 + 2;
                }

                if (fps.get()) {
                    int fpsTextWidth = (int) (CFontUtil.Jello_Light_20.getRenderer().getStringWidth(fpsText) + 7);
                    RenderUtil.rectangle(5, yPos2, fpsTextWidth, CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2, true, new Color(0, 0, 0, 180));
                    RenderUtil.rectangle(5, yPos2, fpsTextWidth, CFontUtil.Jello_Light_20.getRenderer().getHeight() + 2, false, new Color(textRed, textGreen, textBlue, textAlpha));
                    CFontUtil.Jello_Light_20.getRenderer().drawString(fpsText, 7.5, yPos2 + 1, new Color(textRed, textGreen, textBlue, textAlpha));
                }

                if (clock.get()) {
                    int textWidth1 = (int) CFontUtil.Jello_Light_20.getRenderer().getStringWidth(timeStamp);
                    int textHeight1 = (int) CFontUtil.Jello_Light_20.getRenderer().getHeight();
                    RenderUtil.rectangle(rectX, rectY, clockRectWidth, clockRectHeight, true, new Color(0, 0, 0, 180));
                    RenderUtil.rectangle(rectX, rectY, clockRectWidth, clockRectHeight, false, new Color(textRed, textGreen, textBlue, textAlpha));
                    int textX = rectX + (clockRectWidth - textWidth1) / 2;
                    int textY = rectY + (clockRectHeight - textHeight1) / 2;
                    CFontUtil.Jello_Light_20.getRenderer().drawString(timeStamp, textX, textY, new Color(textRed, textGreen, textBlue, textAlpha));
                }
                break;

        }
    }

    @Override
    public void renderDummy() {
        renderIngame();
    }
}
