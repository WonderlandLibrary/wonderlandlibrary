// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.module.impl.player.ChestStealer;
import dev.tenacity.module.impl.player.InvManager;
import dev.tenacity.module.impl.combat.KillAura;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Collections;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import java.util.Date;
import java.text.SimpleDateFormat;
import dev.tenacity.utils.render.Theme;
import net.minecraft.client.gui.GuiNewChat;
import java.util.Iterator;
import java.util.List;
import dev.tenacity.utils.misc.RomanNumeralUtils;
import net.minecraft.potion.Potion;
import java.util.Comparator;
import net.minecraft.client.resources.I18n;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiChat;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.player.MovementUtils;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.font.CustomFont;
import dev.tenacity.utils.font.AbstractFontRenderer;
import dev.tenacity.utils.server.PingerUtils;
import dev.tenacity.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import dev.tenacity.utils.render.GradientUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.tuples.Pair;
import java.awt.Color;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.settings.Setting;
import java.util.LinkedHashMap;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.Category;
import java.util.Map;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.Module;

public class HUDMod extends Module
{
    private final StringSetting clientName;
    private final ModeSetting watermarkMode;
    public static final ColorSetting color1;
    public static final ColorSetting color2;
    public static final ModeSetting theme;
    public static final BooleanSetting customFont;
    private static final MultipleBoolSetting infoCustomization;
    public static final MultipleBoolSetting hudCustomization;
    private static final MultipleBoolSetting disableButtons;
    public static int offsetValue;
    private final Animation fadeInText;
    private int ticks;
    private boolean version;
    public static float xOffset;
    private final Map<String, String> bottomLeftText;
    
    public HUDMod() {
        super("HUD", "HUD", Category.RENDER, "customizes the client's appearance");
        this.clientName = new StringSetting("Client Name");
        this.watermarkMode = new ModeSetting("Watermark Mode", "Tenacity", new String[] { "Tenacity", "Plain Text", "Neverlose", "Tenasense", "Tenabition", "Logo", "None" });
        this.fadeInText = new DecelerateAnimation(500, 1.0);
        this.ticks = 0;
        this.version = true;
        this.bottomLeftText = new LinkedHashMap<String, String>();
        HUDMod.color1.addParent(HUDMod.theme, modeSetting -> modeSetting.is("Custom Theme"));
        HUDMod.color2.addParent(HUDMod.theme, modeSetting -> modeSetting.is("Custom Theme") && !HUDMod.color1.isRainbow());
        this.addSettings(this.clientName, this.watermarkMode, HUDMod.theme, HUDMod.color1, HUDMod.color2, HUDMod.customFont, HUDMod.infoCustomization, HUDMod.hudCustomization, HUDMod.disableButtons);
        if (!this.enabled) {
            this.toggleSilent();
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        Pair<Color, Color> clientColors = getClientColors();
        String name = "Tenacity";
        if (e.isBloom()) {
            final boolean glow = e.getBloomOptions().getSetting("Watermark").isEnabled();
            if (!glow) {
                clientColors = Pair.of(Color.BLACK);
            }
            if (!this.clientName.getString().equals("")) {
                name = this.clientName.getString().replace("%time%", getCurrentTimeStamp());
            }
            final String finalName = get(name);
            final String intentInfo = Tenacity.INSTANCE.getIntentAccount().username;
            final String mode = this.watermarkMode.getMode();
            switch (mode) {
                case "Logo": {
                    final float WH = 55.0f;
                    final float textWidth = HUDMod.tenacityBoldFont32.getStringWidth(finalName);
                    GL11.glEnable(3089);
                    RenderUtil.scissor(10.0, 7.0, 13.0f + WH + textWidth + 5.0f, WH);
                    HUDMod.tenacityBoldFont32.drawString(finalName, 13.0f + WH - textWidth + textWidth * this.fadeInText.getOutput().floatValue(), 8.0f + HUDMod.tenacityBoldFont32.getMiddleOfBox(WH), ColorUtil.applyOpacity(glow ? -1 : 0, this.fadeInText.getOutput().floatValue()));
                    GL11.glDisable(3089);
                    final float n2;
                    GradientUtil.applyGradientCornerLR(27.0f, 23.0f, WH - 28.0f, WH - 28.0f, 1.0f, clientColors.getSecond(), clientColors.getFirst(), () -> {
                        HUDMod.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/watermarkBack.png"));
                        Gui.drawModalRectWithCustomSizedTexture(7.0f, 7.0f, 0.0f, 0.0f, n2, n2, n2, n2);
                        return;
                    });
                    break;
                }
                case "Tenacity": {
                    final float xVal = 6.0f;
                    final float yVal = 6.0f;
                    final float versionWidth = HUDMod.tenacityFont16.getStringWidth(Tenacity.INSTANCE.getVersion());
                    final float versionX = xVal + HUDMod.tenacityBoldFont40.getStringWidth(finalName);
                    final float width = this.version ? (versionX + versionWidth - xVal) : HUDMod.tenacityBoldFont40.getStringWidth(finalName);
                    RenderUtil.resetColor();
                    final String name2;
                    final float x2;
                    final float n3;
                    final float x3;
                    GradientUtil.applyGradientHorizontal(xVal, yVal, width, 20.0f, 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
                        RenderUtil.setAlphaLimit(0.0f);
                        HUDMod.tenacityBoldFont40.drawString(name2, x2, n3, 0);
                        if (this.version) {
                            HUDMod.tenacityFont16.drawString(Tenacity.INSTANCE.getVersion(), x3, n3, 0);
                        }
                        return;
                    });
                    break;
                }
                case "Plain Text": {
                    AbstractFontRenderer fr = HUDMod.mc.fontRendererObj;
                    if (HUDMod.customFont.isEnabled()) {
                        fr = HUDMod.tenacityFont24;
                    }
                    final AbstractFontRenderer finalFr = fr;
                    final AbstractFontRenderer abstractFontRenderer;
                    final String s;
                    GradientUtil.applyGradientHorizontal(5.0f, 5.0f, finalFr.getStringWidth(finalName), (float)finalFr.getHeight(), 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
                        RenderUtil.setAlphaLimit(0.0f);
                        abstractFontRenderer.drawString(s, 5.5f, 5.5f, new Color(0, 0, 0, 0).getRGB());
                        return;
                    });
                    break;
                }
                case "Neverlose": {
                    final CustomFont t18 = HUDMod.tenacityFont18;
                    final String str = String.format(" §8|§f %s fps §8|§f %s §8|§f %s", Minecraft.getDebugFPS(), intentInfo, (HUDMod.mc.isSingleplayer() || HUDMod.mc.getCurrentServerData() == null) ? "singleplayer" : HUDMod.mc.getCurrentServerData().serverIP);
                    name = name.toUpperCase();
                    final float nw = HUDMod.neverloseFont.size(22).getStringWidth(name);
                    RoundedUtil.drawRound(4.0f, 4.5f, nw + t18.getStringWidth(str) + 6.0f, (float)(t18.getHeight() + 6), 2.0f, Color.BLACK);
                    break;
                }
                case "Tenasense": {
                    final String text = "§ftena§rsense§f - " + intentInfo + " - " + (HUDMod.mc.isSingleplayer() ? "singleplayer" : HUDMod.mc.getCurrentServerData().serverIP) + " - " + PingerUtils.getPing() + "ms ";
                    final float x = 4.5f;
                    final float y = 4.5f;
                    Gui.drawRect2(x, y, HUDMod.tenacityFont16.getStringWidth(text) + 7.0f, 18.5, glow ? new Color(59, 57, 57).getRGB() : Color.BLACK.getRGB());
                    break;
                }
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        final ScaledResolution sr = new ScaledResolution(HUDMod.mc);
        final Pair<Color, Color> clientColors = getClientColors();
        String name = "Tenacity";
        final PostProcessing postProcessing = Tenacity.INSTANCE.getModuleCollection().getModule(PostProcessing.class);
        if (!postProcessing.isEnabled()) {
            this.version = false;
        }
        if (!this.clientName.getString().equals("")) {
            name = this.clientName.getString().replace("%time%", getCurrentTimeStamp());
        }
        this.version = name.equalsIgnoreCase("Tenacity");
        String finalName = get(name);
        final String intentInfo = Tenacity.INSTANCE.getIntentAccount().username;
        final String mode = this.watermarkMode.getMode();
        switch (mode) {
            case "Logo": {
                final float WH = 55.0f;
                if (MovementUtils.isMoving()) {
                    this.ticks = 0;
                }
                else {
                    this.ticks = Math.min(this.ticks + 1, 301);
                }
                this.fadeInText.setDirection((this.ticks < 300) ? Direction.BACKWARDS : Direction.FORWARDS);
                final float textWidth = HUDMod.tenacityBoldFont32.getStringWidth(finalName);
                GL11.glEnable(3089);
                RenderUtil.scissor(10.0, 7.0, 13.0f + WH + textWidth + 5.0f, WH);
                HUDMod.tenacityBoldFont32.drawString(finalName, 13.0f + WH - textWidth + textWidth * this.fadeInText.getOutput().floatValue(), 8.0f + HUDMod.tenacityBoldFont32.getMiddleOfBox(WH), ColorUtil.applyOpacity(-1, 0.7f * this.fadeInText.getOutput().floatValue()));
                GL11.glDisable(3089);
                RenderUtil.color(Color.BLUE.getRGB());
                final float n2;
                GradientUtil.applyGradientCornerLR(27.0f, 23.0f, WH - 28.0f, WH - 28.0f, 1.0f, clientColors.getSecond(), clientColors.getFirst(), () -> {
                    HUDMod.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/watermarkBack.png"));
                    Gui.drawModalRectWithCustomSizedTexture(7.0f, 7.0f, 0.0f, 0.0f, n2, n2, n2, n2);
                    return;
                });
                RenderUtil.color(-1);
                GLUtil.startBlend();
                HUDMod.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/watermarkT.png"));
                Gui.drawModalRectWithCustomSizedTexture(7.0f, 7.0f, 0.0f, 0.0f, WH, WH, WH, WH);
                break;
            }
            case "Tenacity": {
                final float xVal = 5.0f;
                final float yVal = 5.0f;
                final float spacing = 1.0f;
                final float versionWidth = HUDMod.tenacityFont16.getStringWidth(Tenacity.INSTANCE.getVersion());
                final float versionX = xVal + HUDMod.tenacityBoldFont40.getStringWidth(finalName);
                final float width = this.version ? (versionX + versionWidth - xVal) : HUDMod.tenacityBoldFont40.getStringWidth(finalName);
                final String finalName2 = finalName;
                final Pair<Color, Color> darkerColors = clientColors.apply((c1, c2) -> Pair.of(ColorUtil.darker(c1, 0.6f), ColorUtil.darker(c2, 0.6f)));
                final String name2;
                final Object o;
                final Object o2;
                final float n3;
                final float n4;
                GradientUtil.applyGradientHorizontal(xVal + spacing, yVal + spacing, width + spacing, 20.0f, 1.0f, darkerColors.getFirst(), darkerColors.getSecond(), () -> {
                    RenderUtil.setAlphaLimit(0.0f);
                    HUDMod.tenacityBoldFont40.drawString(name2, (float)(o + o2), (float)(n3 + o2), 0);
                    if (this.version) {
                        HUDMod.tenacityFont16.drawString(Tenacity.INSTANCE.getVersion(), (float)(n4 + o2 / 2.0f), (float)(n3 + o2 / 2.0f), 0);
                    }
                    return;
                });
                RenderUtil.resetColor();
                final String name3;
                final float x2;
                final float n5;
                final float x3;
                GradientUtil.applyGradientHorizontal(xVal, yVal, width, 20.0f, 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
                    RenderUtil.setAlphaLimit(0.0f);
                    HUDMod.tenacityBoldFont40.drawString(name3, x2, n5, 0);
                    if (this.version) {
                        HUDMod.tenacityFont16.drawString(Tenacity.INSTANCE.getVersion(), x3, n5, 0);
                    }
                    return;
                });
                break;
            }
            case "Plain Text": {
                AbstractFontRenderer fr = HUDMod.mc.fontRendererObj;
                if (HUDMod.customFont.isEnabled()) {
                    fr = HUDMod.tenacityBoldFont26;
                }
                final AbstractFontRenderer finalFr = fr;
                finalName = get(name);
                fr.drawString(finalName, 5.5f, 5.5f, Color.BLACK.getRGB());
                final String finalName3 = finalName;
                final AbstractFontRenderer abstractFontRenderer;
                final String s;
                GradientUtil.applyGradientHorizontal(5.0f, 5.0f, fr.getStringWidth(finalName), (float)fr.getHeight(), 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
                    RenderUtil.setAlphaLimit(0.0f);
                    abstractFontRenderer.drawString(s, 5.0f, 5.0f, new Color(0, 0, 0, 0).getRGB());
                    return;
                });
                break;
            }
            case "Neverlose": {
                final CustomFont m22 = HUDMod.neverloseFont.size(22);
                final CustomFont t18 = HUDMod.tenacityFont18;
                final String str = String.format(" §8|§f %s fps §8|§f %s §8|§f %s", Minecraft.getDebugFPS(), intentInfo, (HUDMod.mc.isSingleplayer() || HUDMod.mc.getCurrentServerData() == null) ? "singleplayer" : HUDMod.mc.getCurrentServerData().serverIP);
                name = name.toUpperCase();
                final float nw = m22.getStringWidth(name);
                RoundedUtil.drawRound(4.0f, 4.5f, nw + t18.getStringWidth(str) + 6.0f, (float)(t18.getHeight() + 6), 2.0f, Color.BLACK);
                t18.drawString(str, 7.5f + nw, 7.5f, -1);
                m22.drawString(name, 7.5f, 8.0f, new Color(0, 149, 200).getRGB());
                m22.drawString(name, 7.0f, 7.5f, -1);
                break;
            }
            case "Tenasense": {
                final String text = "§ftena§rsense§f - " + intentInfo + " - " + (HUDMod.mc.isSingleplayer() ? "singleplayer" : HUDMod.mc.getCurrentServerData().serverIP) + " - " + PingerUtils.getPing() + "ms ";
                final float x = 4.5f;
                final float y = 4.5f;
                final int lineColor = new Color(59, 57, 57).darker().getRGB();
                Gui.drawRect2(x, y, HUDMod.tenacityFont16.getStringWidth(text) + 7.0f, 18.5, new Color(59, 57, 57).getRGB());
                Gui.drawRect2(x + 2.5, y + 2.5, HUDMod.tenacityFont16.getStringWidth(text) + 2.0f, 13.0, new Color(23, 23, 23).getRGB());
                Gui.drawRect2(x + 1.0f, y + 1.0f, HUDMod.tenacityFont16.getStringWidth(text) + 5.0f, 0.5, lineColor);
                Gui.drawRect2(x + 1.0f, y + 17.0f, HUDMod.tenacityFont16.getStringWidth(text) + 5.0f, 0.5, lineColor);
                Gui.drawRect2(x + 1.0f, y + 1.5, 0.5, 16.0, lineColor);
                Gui.drawRect2(x + 1.5 + HUDMod.tenacityFont16.getStringWidth(text) + 4.0, y + 1.5, 0.5, 16.0, lineColor);
                GradientUtil.drawGradientLR(x + 2.5f, y + 14.5f, HUDMod.tenacityFont16.getStringWidth(text) + 2.0f, 1.0f, 1.0f, clientColors.getFirst(), clientColors.getSecond());
                Gui.drawRect2(x + 2.5, y + 16.0f, HUDMod.tenacityFont16.getStringWidth(text) + 2.0f, 0.5, lineColor);
                HUDMod.tenacityFont16.drawString(text, x + 4.5f, y + 5.5f, clientColors.getSecond().getRGB());
                break;
            }
            case "Tenabition": {
                final StringBuilder stringBuilder = new StringBuilder(name.replace("tenacity", "Tenabition")).insert(1, "§7");
                stringBuilder.append(" [§fFPS: ").append(Minecraft.getDebugFPS()).append("§7]");
                stringBuilder.append(" [§fTime: ").append(getCurrentTimeStamp()).append("§7]");
                RenderUtil.resetColor();
                HUDMod.mc.fontRendererObj.drawStringWithShadow(stringBuilder.toString(), 4.0f, 4.0f, clientColors.getFirst().getRGB());
                break;
            }
        }
        RenderUtil.resetColor();
        this.drawBottomRight();
        RenderUtil.resetColor();
        this.drawInfo(clientColors);
        this.drawArmor(sr);
    }
    
    private void drawBottomRight() {
        final AbstractFontRenderer fr = HUDMod.customFont.isEnabled() ? HUDMod.tenacityFont20 : HUDMod.mc.fontRendererObj;
        final ScaledResolution sr = new ScaledResolution(HUDMod.mc);
        final float yOffset = (float)(14.5 * GuiChat.openingAnimation.getOutput().floatValue());
        final boolean shadowInfo = HUDMod.infoCustomization.isEnabled("Info Shadow");
        if (HUDMod.hudCustomization.getSetting("Potion HUD").isEnabled()) {
            final List<PotionEffect> potions = new ArrayList<PotionEffect>(HUDMod.mc.thePlayer.getActivePotionEffects());
            potions.sort(Comparator.comparingDouble(e -> -fr.getStringWidth(I18n.format(e.getEffectName(), new Object[0]))));
            int count = 0;
            for (final PotionEffect effect : potions) {
                final Potion potion = Potion.potionTypes[effect.getPotionID()];
                final String name = I18n.format(potion.getName(), new Object[0]) + ((effect.getAmplifier() > 0) ? (" " + RomanNumeralUtils.generate(effect.getAmplifier() + 1)) : "");
                final Color c = new Color(potion.getLiquidColor());
                final String str = get(name + " §7[" + Potion.getDurationString(effect) + "]");
                fr.drawString(str, sr.getScaledWidth() - fr.getStringWidth(str) - 2.0f, -10 + sr.getScaledHeight() - fr.getHeight() + (7 - 10 * (count + 1)) - yOffset, new Color(c.getRed(), c.getGreen(), c.getBlue(), 255).getRGB(), shadowInfo);
                ++count;
            }
            HUDMod.offsetValue = count * fr.getHeight();
        }
        String text = "6.0 - " + (HUDMod.customFont.isEnabled() ? "" : "§l") + Tenacity.RELEASE.getName() + "§r";
        text = text + " | " + Tenacity.INSTANCE.getIntentAccount().username;
        text = text + " (" + Tenacity.INSTANCE.getIntentAccount().client_uid + ")";
        text = get(text);
        final float x = sr.getScaledWidth() - (fr.getStringWidth(text) + 3.0f);
        final float y = sr.getScaledHeight() - (fr.getHeight() + 3) - yOffset;
        final Pair<Color, Color> clientColors = getClientColors();
        final String finalText = text;
        final float f = HUDMod.customFont.isEnabled() ? 0.5f : 1.0f;
        fr.drawString(finalText, x + f, y + f, -16777216);
        final AbstractFontRenderer abstractFontRenderer;
        final String s;
        final float n;
        final float n2;
        GradientUtil.applyGradientHorizontal(x, y, fr.getStringWidth(text), 20.0f, 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
            RenderUtil.setAlphaLimit(0.0f);
            abstractFontRenderer.drawString(s, n, n2, -1);
        });
    }
    
    private void drawInfo(final Pair<Color, Color> clientColors) {
        final boolean shadowInfo = HUDMod.infoCustomization.isEnabled("Info Shadow");
        final boolean semiBold = HUDMod.infoCustomization.isEnabled("Semi-Bold Info");
        final boolean whiteInfo = HUDMod.infoCustomization.isEnabled("White Info");
        final String titleBold = semiBold ? "§l" : "";
        final ScaledResolution sr = new ScaledResolution(HUDMod.mc);
        this.bottomLeftText.put("XYZ", Math.round(HUDMod.mc.thePlayer.posX) + " " + Math.round(HUDMod.mc.thePlayer.posY) + " " + Math.round(HUDMod.mc.thePlayer.posZ));
        this.bottomLeftText.put("Speed", String.valueOf(this.calculateBPS()));
        this.bottomLeftText.put("FPS", String.valueOf(Minecraft.getDebugFPS()));
        if (HUDMod.infoCustomization.isEnabled("Show Ping")) {
            this.bottomLeftText.put("Ping", PingerUtils.getPing());
            GuiNewChat.chatPos = 10;
        }
        else {
            GuiNewChat.chatPos = 17;
            this.bottomLeftText.remove("Ping");
        }
        AbstractFontRenderer nameInfoFr = HUDMod.tenacityFont20;
        if (!HUDMod.customFont.isEnabled()) {
            nameInfoFr = HUDMod.mc.fontRendererObj;
        }
        if (semiBold) {
            HUDMod.xOffset = nameInfoFr.getStringWidth("§lXYZ: " + this.bottomLeftText.get("XYZ"));
        }
        else {
            HUDMod.xOffset = nameInfoFr.getStringWidth("XYZ: " + this.bottomLeftText.get("XYZ"));
        }
        final float yOffset = (float)(14.5 * GuiChat.openingAnimation.getOutput().floatValue());
        final float f2 = HUDMod.customFont.isEnabled() ? 0.5f : 1.0f;
        final float f3 = HUDMod.customFont.isEnabled() ? 1.0f : 0.5f;
        final float yMovement = HUDMod.customFont.isEnabled() ? 0.0f : -1.0f;
        if (whiteInfo) {
            float boldFontMovement = nameInfoFr.getHeight() + 2 + yOffset + yMovement;
            for (final Map.Entry<String, String> line : this.bottomLeftText.entrySet()) {
                nameInfoFr.drawString(get(titleBold + line.getKey() + "§r: " + line.getValue()), 2.0f, sr.getScaledHeight() - boldFontMovement, -1, shadowInfo);
                boldFontMovement += nameInfoFr.getHeight() + f3;
            }
        }
        else {
            float f4 = nameInfoFr.getHeight() + 2 + yOffset + yMovement;
            for (final Map.Entry<String, String> line : this.bottomLeftText.entrySet()) {
                if (shadowInfo) {
                    nameInfoFr.drawString(get(line.getValue()), 2.0f + f2 + nameInfoFr.getStringWidth(titleBold + line.getKey() + ":§r "), sr.getScaledHeight() - f4 + f2, -16777216);
                }
                nameInfoFr.drawString(get(line.getValue()), 2.0f + nameInfoFr.getStringWidth(titleBold + line.getKey() + ":§r "), sr.getScaledHeight() - f4, -1);
                f4 += nameInfoFr.getHeight() + f3;
            }
            final float height = (float)((nameInfoFr.getHeight() + 2) * this.bottomLeftText.size());
            final float width = nameInfoFr.getStringWidth(titleBold + "Speed:");
            final AbstractFontRenderer finalFr = nameInfoFr;
            if (shadowInfo) {
                float boldFontMovement2 = finalFr.getHeight() + 2 + yOffset + yMovement;
                for (final Map.Entry<String, String> line2 : this.bottomLeftText.entrySet()) {
                    finalFr.drawString(get(titleBold + line2.getKey() + ": "), 2.0f + f2, sr.getScaledHeight() - boldFontMovement2 + f2, -16777216);
                    boldFontMovement2 += finalFr.getHeight() + f3;
                }
            }
            final AbstractFontRenderer abstractFontRenderer;
            final float n;
            final float n2;
            float boldFontMovement3;
            final Iterator<Map.Entry<String, String>> iterator4;
            Map.Entry<String, String> line3;
            final String str;
            final ScaledResolution scaledResolution;
            final float n3;
            GradientUtil.applyGradientVertical(2.0f, sr.getScaledHeight() - (height + yOffset + yMovement), width, height, 1.0f, clientColors.getFirst(), clientColors.getSecond(), () -> {
                boldFontMovement3 = abstractFontRenderer.getHeight() + 2 + n + n2;
                this.bottomLeftText.entrySet().iterator();
                while (iterator4.hasNext()) {
                    line3 = iterator4.next();
                    abstractFontRenderer.drawString(get(str + line3.getKey() + ": "), 2.0f, scaledResolution.getScaledHeight() - boldFontMovement3, -1);
                    boldFontMovement3 += abstractFontRenderer.getHeight() + n3;
                }
            });
        }
    }
    
    private double calculateBPS() {
        final double bps = Math.hypot(HUDMod.mc.thePlayer.posX - HUDMod.mc.thePlayer.prevPosX, HUDMod.mc.thePlayer.posZ - HUDMod.mc.thePlayer.prevPosZ) * HUDMod.mc.timer.timerSpeed * 20.0;
        return Math.round(bps * 100.0) / 100.0;
    }
    
    public static Pair<Color, Color> getClientColors() {
        return Theme.getThemeColors(HUDMod.theme.getMode());
    }
    
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }
    
    public static String get(final String text) {
        return HUDMod.hudCustomization.getSetting("Lowercase").isEnabled() ? text.toLowerCase() : text;
    }
    
    private void drawArmor(final ScaledResolution sr) {
        if (HUDMod.hudCustomization.getSetting("Armor HUD").isEnabled()) {
            final List<ItemStack> equipment = new ArrayList<ItemStack>();
            final boolean inWater = HUDMod.mc.thePlayer.isEntityAlive() && HUDMod.mc.thePlayer.isInsideOfMaterial(Material.water);
            int x = -74;
            for (int i = 3; i >= 0; --i) {
                final ItemStack armorPiece;
                if ((armorPiece = HUDMod.mc.thePlayer.inventory.armorInventory[i]) != null) {
                    equipment.add(armorPiece);
                }
            }
            final ItemStack heldItem = HUDMod.mc.thePlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() != null) {
                equipment.add(heldItem);
            }
            Collections.reverse(equipment);
            final Iterator<ItemStack> iterator = equipment.iterator();
            while (iterator.hasNext()) {
                final ItemStack armorPiece;
                final ItemStack itemStack = armorPiece = iterator.next();
                RenderHelper.enableGUIStandardItemLighting();
                x += 15;
                if (armorPiece == HUDMod.mc.thePlayer.getHeldItem()) {
                    x -= 15;
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                HUDMod.mc.getRenderItem().zLevel = -200.0f;
                final int s = HUDMod.mc.thePlayer.capabilities.isCreativeMode ? 15 : 0;
                HUDMod.mc.getRenderItem().renderItemAndEffectIntoGUI(armorPiece, -x + sr.getScaledWidth() / 2 - 4, (int)(sr.getScaledHeight() - (inWater ? 65 : 55) + s - 5 - 16.0f * GuiChat.openingAnimation.getOutput().floatValue()));
                if (armorPiece.stackSize > 1) {
                    final String count = String.valueOf(armorPiece.stackSize);
                    HUDMod.mc.fontRendererObj.drawStringWithShadow(count, -x + sr.getScaledWidth() / 2 - 4 + 19 - 2 - HUDMod.mc.fontRendererObj.getStringWidth(count), sr.getScaledHeight() - (inWater ? 65 : 55) + s - 5 - 16.0f * GuiChat.openingAnimation.getOutput().floatValue() + 9.0f, 16777215);
                }
                HUDMod.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.disableBlend();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
                armorPiece.getEnchantmentTagList();
                RenderUtil.drawExhiEnchants(armorPiece, (float)(-x + sr.getScaledWidth() / 2 - 4), sr.getScaledHeight() - (inWater ? 65 : 55) + s - 5 - 16.0f * GuiChat.openingAnimation.getOutput().floatValue());
            }
        }
    }
    
    public static boolean isRainbowTheme() {
        return HUDMod.theme.is("Custom Theme") && HUDMod.color1.isRainbow();
    }
    
    public static boolean drawRadialGradients() {
        return HUDMod.hudCustomization.getSetting("Radial Gradients").isEnabled();
    }
    
    public static void addButtons(final List<GuiButton> buttonList) {
        for (final ModuleButton mb : ModuleButton.values()) {
            if (mb.getSetting().isEnabled()) {
                buttonList.add(mb.getButton());
            }
        }
    }
    
    public static void updateButtonStatus() {
        for (final ModuleButton mb : ModuleButton.values()) {
            mb.getButton().enabled = Tenacity.INSTANCE.getModuleCollection().getModule(mb.getModule()).isEnabled();
        }
    }
    
    public static void handleActionPerformed(final GuiButton button) {
        final ModuleButton[] values = ModuleButton.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final ModuleButton mb = values[i];
            if (mb.getButton() == button) {
                final Module m = Tenacity.INSTANCE.getModuleCollection().getModule(mb.getModule());
                if (m.isEnabled()) {
                    m.toggle();
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
    }
    
    static {
        color1 = new ColorSetting("Color 1", new Color(-6281004));
        color2 = new ColorSetting("Color 2", new Color(-16774913));
        theme = Theme.getModeSetting("Theme Selection", "Tenacity");
        customFont = new BooleanSetting("Custom Font", true);
        infoCustomization = new MultipleBoolSetting("Info Options", new BooleanSetting[] { new BooleanSetting("Show Ping", false), new BooleanSetting("Semi-Bold Info", true), new BooleanSetting("White Info", false), new BooleanSetting("Info Shadow", true) });
        hudCustomization = new MultipleBoolSetting("HUD Options", new BooleanSetting[] { new BooleanSetting("Radial Gradients", true), new BooleanSetting("Potion HUD", true), new BooleanSetting("Armor HUD", true), new BooleanSetting("Render Cape", true), new BooleanSetting("Lowercase", false) });
        disableButtons = new MultipleBoolSetting("Disable Buttons", new BooleanSetting[] { new BooleanSetting("Disable KillAura", true), new BooleanSetting("Disable InvManager", true), new BooleanSetting("Disable ChestStealer", true) });
        HUDMod.offsetValue = 0;
        HUDMod.xOffset = 0.0f;
    }
    
    public enum ModuleButton
    {
        AURA((Class<? extends Module>)KillAura.class, HUDMod.disableButtons.getSetting("Disable KillAura"), new GuiButton(2461, 3, 4, 120, 20, "Disable KillAura")), 
        INVMANAGER((Class<? extends Module>)InvManager.class, HUDMod.disableButtons.getSetting("Disable InvManager"), new GuiButton(2462, 3, 26, 120, 20, "Disable InvManager")), 
        CHESTSTEALER((Class<? extends Module>)ChestStealer.class, HUDMod.disableButtons.getSetting("Disable ChestStealer"), new GuiButton(2463, 3, 48, 120, 20, "Disable ChestStealer"));
        
        private final Class<? extends Module> module;
        private final BooleanSetting setting;
        private final GuiButton button;
        
        public Class<? extends Module> getModule() {
            return this.module;
        }
        
        public BooleanSetting getSetting() {
            return this.setting;
        }
        
        public GuiButton getButton() {
            return this.button;
        }
        
        private ModuleButton(final Class<? extends Module> module, final BooleanSetting setting, final GuiButton button) {
            this.module = module;
            this.setting = setting;
            this.button = button;
        }
    }
}
