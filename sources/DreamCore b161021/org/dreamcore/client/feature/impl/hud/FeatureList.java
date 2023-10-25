package org.dreamcore.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class FeatureList extends Feature {

    public static ListSetting sortMode;
    public static ListSetting fontRenderType;
    public static ListSetting borderMode;

    public static BooleanSetting backGround;
    public static BooleanSetting backGroundGradient = new BooleanSetting("BackGround Gradient", false, () -> backGround.getBoolValue());
    public static ColorSetting backGroundColor2 = new ColorSetting("BackGround Color Two", Color.BLACK.getRGB(), () -> backGround.getBoolValue() && backGroundGradient.getBoolValue());
    public static ColorSetting backGroundColor = new ColorSetting("BackGround Color", Color.BLACK.getRGB(), () -> backGround.getBoolValue());
    public static BooleanSetting border;
    public static BooleanSetting rightBorder;
    public static NumberSetting x;
    public static NumberSetting y;
    public static NumberSetting offset;
    public static NumberSetting size;
    public static NumberSetting borderWidth;
    public static NumberSetting rainbowSaturation;
    public static NumberSetting rainbowBright;
    public static NumberSetting fontX;
    public static NumberSetting fontY;
    public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> backGround.getBoolValue());
    public static BooleanSetting suffix;
    public static ListSetting colorSuffixMode = new ListSetting("Suffix Mode Color", "Default", () -> suffix.getBoolValue(), "Astolfo", "Default", "Static", "Rainbow", "Custom", "Category");
    public static ColorSetting suffixColor = new ColorSetting("Suffix Color", Color.GRAY.getRGB(), () -> colorSuffixMode.currentMode.equals("Custom") || colorSuffixMode.currentMode.equals("Static") && suffix.getBoolValue());
    public static ListSetting position = new ListSetting("Position", "Right", () -> true, "Right", "Left");

    public FeatureList() {
        super("FeatureList", "Показывает список всех включенных модулей", Type.Hud);

        /* COLOR SETTINGS */

        /* OTHER */

        borderMode = new ListSetting("Border Mode", "Full", () -> border.getBoolValue(), "Full", "Single");
        sortMode = new ListSetting("FeatureList Sort", "Length", () -> true, "Length", "Alphabetical");
        fontRenderType = new ListSetting("FontRender Type", "Shadow", () -> true, "Default", "Shadow", "Outline");
        backGround = new BooleanSetting("Background", true, () -> true);
        border = new BooleanSetting("Border", true, () -> true);
        rightBorder = new BooleanSetting("Right Border", true, () -> true);
        suffix = new BooleanSetting("Suffix", true, () -> true);
        //    alpha = new NumberSetting("BackgroundAlpha", 1, 1, 255, 1, () -> backGround.getCurrentValue() && !blur.getCurrentValue());
        //   bright = new NumberSetting("BackgroundBright", 255, 1, 255, 1, () -> backGround.getCurrentValue() && !blur.getCurrentValue());
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1F, 0.1F, () -> HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"));
        rainbowBright = new NumberSetting("Rainbow Brightness", 1F, 0.1F, 1F, 0.1F, () -> HUD.colorList.currentMode.equals("Rainbow") || colorSuffixMode.currentMode.equals("Rainbow"));
        fontX = new NumberSetting("FontX", 0, -4, 20, 0.1F, () -> true);
        fontY = new NumberSetting("FontY", 0, -4, 20, 0.01F, () -> true);
        x = new NumberSetting("FeatureList X", 0, 0, 500, 1, () -> !blur.getBoolValue());
        y = new NumberSetting("FeatureList Y", 0, 0, 500, 1, () -> !blur.getBoolValue());
        offset = new NumberSetting("Font Offset", 11, 7, 20, 0.5F, () -> true);
        borderWidth = new NumberSetting("Border Width", 1, 0, 10, 0.1F, () -> rightBorder.getBoolValue());
        addSettings(position, sortMode, fontRenderType, borderMode, colorSuffixMode, suffixColor, fontX, fontY, border, rightBorder, suffix, borderWidth, backGround, backGroundGradient, backGroundColor, backGroundColor2, rainbowSaturation, rainbowBright, x, y, offset);
    }

    private static Feature getNextEnabledFeature(List<Feature> features, int index) {
        for (int i = index; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.getState() && feature.visible) {
                if (!feature.getSuffix().equals("ClickGui") && feature.visible) {
                    return feature;
                }
            }
        }
        return null;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = HUD.colorList.getCurrentMode();
        setSuffix(mode);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        float width = event.getResolution().getScaledWidth() - (FeatureList.rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        float y = 1;
        String arraySort = sortMode.getCurrentMode();
        if (dreamcore.instance.featureManager.getFeatureByClass(FeatureList.class).getState() && !mc.gameSettings.showDebugInfo) {
            dreamcore.instance.featureManager.getFeatureList().sort(arraySort.equalsIgnoreCase("Alphabetical") ? Comparator.comparing(Feature::getLabel) : Comparator.comparingInt(module -> !HUD.font.currentMode.equals("Minecraft") ? -ClientHelper.getFontRender().getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel()) : -mc.fontRendererObj.getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel())));
            for (Feature feature : dreamcore.instance.featureManager.getFeatureList()) {
                ScreenHelper animationHelper = feature.getScreenHelper();
                String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();
                float listOffset = FeatureList.offset.getNumberValue();
                float length = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getStringWidth(featureSuffix) : mc.fontRendererObj.getStringWidth(featureSuffix);
                float featureX = width - length;
                boolean state = feature.getState() && feature.visible;

                if (state) {
                    animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 6);
                } else {
                    animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 6);
                }

                float yPotion = 2;

                for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
                    if (potionEffect.getPotion().isBeneficial()) {
                        yPotion = 26;
                    }
                    if (potionEffect.getPotion().isBadEffect()) {
                        yPotion = 26 * 2;
                    }
                }

                float translateY = animationHelper.getY() + yPotion;
                float translateX = animationHelper.getX() - (FeatureList.rightBorder.getBoolValue() ? 2.5F : 1.5F) - FeatureList.fontX.getNumberValue();
                int color = 0;
                int colorCustom = HUD.onecolor.getColorValue();
                int colorCustom2 = HUD.twocolor.getColorValue();
                double time = HUD.time.getNumberValue();
                String mode = HUD.colorList.getOptions();
                boolean visible = animationHelper.getX() < width;

                if (visible) {
                    switch (mode.toLowerCase()) {
                        case "rainbow":
                            color = PaletteHelper.rainbow((int) (y * time), FeatureList.rainbowSaturation.getNumberValue(), FeatureList.rainbowBright.getNumberValue()).getRGB();
                            break;
                        case "astolfo":
                            color = PaletteHelper.astolfo(false, (int) y * 4).getRGB();
                            break;
                        case "static":
                            color = new Color(colorCustom).getRGB();
                            break;
                        case "custom":
                            color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
                            break;
                        case "fade":
                            color = PaletteHelper.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom).darker().darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 60 * 2) % 2)));
                            break;
                        case "none":
                            color = -1;
                            break;
                        case "category":
                            color = feature.getType().getColor();
                            break;
                    }

                    int colorFuffix = 0;
                    String modeSuffix = FeatureList.colorSuffixMode.getOptions();
                    switch (modeSuffix.toLowerCase()) {
                        case "rainbow":
                            colorFuffix = PaletteHelper.rainbow((int) (y * time), FeatureList.rainbowSaturation.getNumberValue(), FeatureList.rainbowBright.getNumberValue()).getRGB();
                            break;
                        case "astolfo":
                            colorFuffix = PaletteHelper.astolfo(false, (int) y * 4).getRGB();
                            break;
                        case "static":
                            colorFuffix = new Color(suffixColor.getColorValue()).getRGB();
                            break;
                        case "default":
                            colorFuffix = new Color(192, 192, 192).getRGB();
                            break;
                        case "category":
                            colorFuffix = feature.getType().getColor();
                            break;
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(-FeatureList.x.getNumberValue(), FeatureList.y.getNumberValue(), 1);

                    Feature nextFeature = null;
                    int index = dreamcore.instance.featureManager.getFeatureList().indexOf(feature) + 1;

                    if (dreamcore.instance.featureManager.getFeatureList().size() > index) {
                        nextFeature = getNextEnabledFeature(dreamcore.instance.featureManager.getFeatureList(), index);
                    }


                    if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                        RectHelper.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                    }

                    if (nextFeature != null && borderMode.currentMode.equals("Full")) {
                        String name = suffix.getBoolValue() ? nextFeature.getSuffix() : nextFeature.getLabel();
                        float font = !HUD.font.currentMode.equals("Minecraft") ? ClientHelper.getFontRender().getStringWidth(name) : mc.fontRendererObj.getStringWidth(name);
                        float dif = (length - font);
                        if (FeatureList.border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                            RectHelper.drawRect(translateX - 3.5, translateY + listOffset + 1, translateX - 2 + dif, translateY + listOffset - 1, color);
                        }
                    } else {
                        if (FeatureList.border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                            RectHelper.drawRect(translateX - 3.5, translateY + listOffset + 1, width, translateY + listOffset - 1, color);
                        }
                    }

                    if (borderMode.currentMode.equals("Single") && border.getBoolValue()) {
                        RectHelper.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                    }

                    if (FeatureList.backGround.getBoolValue()) {
                        if (!backGroundGradient.getBoolValue()) {
                            RectHelper.drawRect(translateX - 2, translateY - 1, width, translateY + listOffset - 1, backGroundColor.getColorValue());
                        } else {
                            RectHelper.drawGradientRect(translateX - 2, translateY - 1, width, translateY + listOffset - 1, backGroundColor.getColorValue(), backGroundColor2.getColorValue());
                        }
                    }

                    if (!HUD.font.currentMode.equals("Minecraft")) {
                        String modeArrayFont = HUD.font.getOptions();
                        float yOffset = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : modeArrayFont.equalsIgnoreCase("Comfortaa") ? 3 : modeArrayFont.equalsIgnoreCase("CircleRegular") ? 0.5f : modeArrayFont.equalsIgnoreCase("Arial") ? 1.3f : modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9f : modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3f : modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3f : modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5f : modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : 2f;
                        if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Shadow")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().drawStringWithShadow(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                            }
                            ClientHelper.getFontRender().drawStringWithShadow(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Default")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().drawString(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                            }
                            ClientHelper.getFontRender().drawString(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        } else if (!HUD.font.currentMode.equals("Minecraft") && fontRenderType.currentMode.equals("Outline")) {
                            if (suffix.getBoolValue()) {
                                ClientHelper.getFontRender().drawStringWithOutline(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), colorFuffix);
                            }
                            ClientHelper.getFontRender().drawStringWithOutline(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);
                        }
                    } else if (fontRenderType.currentMode.equals("Shadow")) {
                        if (suffix.getBoolValue()) {
                            mc.fontRendererObj.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                        }
                        mc.fontRendererObj.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    } else if (fontRenderType.currentMode.equals("Default")) {
                        if (suffix.getBoolValue()) {
                            mc.fontRendererObj.drawString(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                        }
                        mc.fontRendererObj.drawString(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    } else if (fontRenderType.currentMode.equals("Outline")) {
                        if (suffix.getBoolValue()) {
                            mc.fontRendererObj.drawStringWithOutline(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), colorFuffix);
                        }
                        mc.fontRendererObj.drawStringWithOutline(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                    }

                    y += listOffset;

                    if (FeatureList.rightBorder.getBoolValue()) {
                        float checkY = border.getBoolValue() ? 0 : 0.6F;
                        RectHelper.drawRect(width, translateY - 1, width + borderWidth.getNumberValue(), translateY + listOffset - checkY, color);
                    }

                    GlStateManager.popMatrix();
                }
            }
        }
    }
}