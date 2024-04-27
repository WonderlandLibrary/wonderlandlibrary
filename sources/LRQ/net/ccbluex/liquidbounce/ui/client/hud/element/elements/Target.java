/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.OpenGlHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import jx.utils.EntityUtils2;
import jx.utils.render.RenderUtils;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Palette;
import net.ccbluex.liquidbounce.utils.PlayerUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Target")
public final class Target
extends Element {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"WaterMelon", "SparklingWater", "Best", "Novoline", "Astolfo", "Liquid", "Flux", "Rise", "Zamorozka", "novoline2", "moon", "novoline3", "newnovoline", "tenacity"}, "Rise");
    private final ListValue switchModeValue = new ListValue("SwitchMode", new String[]{"Slide", "Zoom", "None"}, "Slide");
    private final IntegerValue animSpeedValue = new IntegerValue("AnimSpeed", 10, 5, 20);
    private final IntegerValue switchAnimSpeedValue = new IntegerValue("SwitchAnimSpeed", 20, 5, 40);
    private final FontValue fontValue = new FontValue("Font", Fonts.font40);
    private final IntegerValue backgroundalpha = new IntegerValue("Alpha", 120, 0, 255);
    private final IntegerValue redValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue greenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);
    private final IntegerValue gredValue = new IntegerValue("GradientRed", 255, 0, 255);
    private final IntegerValue ggreenValue = new IntegerValue("GradientGreen", 255, 0, 255);
    private final IntegerValue gblueValue = new IntegerValue("GradientBlue", 255, 0, 255);
    private float easingHP;
    private IEntityLivingBase prevTarget;
    private float lastHealth = 20.0f;
    private final FloatValue radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
    private float lastChangeHealth = 20.0f;
    private long changeTime = System.currentTimeMillis();
    private float displayPercent;
    private long lastUpdate = System.currentTimeMillis();
    private final DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private final int[] counter1 = new int[]{50};
    private final int[] counter2 = new int[]{80};

    public final IntegerValue getBackgroundalpha() {
        return this.backgroundalpha;
    }

    private final float getHealth(IEntityLivingBase entity) {
        return entity == null || entity.isDead() ? 0.0f : entity.getHealth();
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public Border drawElement() {
        v0 = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        target = ((KillAura)v0).getTarget();
        time = System.currentTimeMillis();
        pct = (float)(time - this.lastUpdate) / (((Number)this.switchAnimSpeedValue.get()).floatValue() * 50.0f);
        this.lastUpdate = System.currentTimeMillis();
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            target = MinecraftInstance.mc.getThePlayer();
        }
        if (target != null) {
            this.prevTarget = target;
        }
        if (this.prevTarget == null) {
            return this.getTBorder();
        }
        if (target != null) {
            if (this.displayPercent < (float)true) {
                this.displayPercent += pct;
            }
            if (this.displayPercent > (float)true) {
                this.displayPercent = 1.0f;
            }
        } else {
            if (this.displayPercent > (float)false) {
                this.displayPercent -= pct;
            }
            if (this.displayPercent < (float)false) {
                this.displayPercent = 0.0f;
                this.prevTarget = null;
                return this.getTBorder();
            }
        }
        if (this.getHealth(this.prevTarget) != this.lastHealth) {
            this.lastChangeHealth = this.lastHealth;
            this.lastHealth = this.getHealth(this.prevTarget);
            this.changeTime = time;
        }
        nowAnimHP = time - (long)(((Number)this.animSpeedValue.get()).intValue() * 50) < this.changeTime ? this.getHealth(this.prevTarget) + (this.lastChangeHealth - this.getHealth(this.prevTarget)) * ((float)true - (float)(time - this.changeTime) / (((Number)this.animSpeedValue.get()).floatValue() * 50.0f)) : this.getHealth(this.prevTarget);
        var6_5 = (String)this.switchModeValue.get();
        var7_6 = false;
        v1 = var6_5;
        if (v1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var6_5 = v1.toLowerCase();
        switch (var6_5.hashCode()) {
            case 109526449: {
                if (!var6_5.equals("slide")) ** break;
                break;
            }
            case 3744723: {
                if (!var6_5.equals("zoom")) ** break;
                v2 = this.getTBorder();
                if (v2 == null) {
                    return null;
                }
                border = v2;
                GL11.glScalef((float)this.displayPercent, (float)this.displayPercent, (float)this.displayPercent);
                GL11.glTranslatef((float)(border.getX2() * 0.5f * ((float)true - this.displayPercent) / this.displayPercent), (float)(border.getY2() * 0.5f * ((float)true - this.displayPercent) / this.displayPercent), (float)0.0f);
                ** break;
            }
        }
        percent = EaseUtils.INSTANCE.easeInQuint(1.0 - (double)this.displayPercent);
        xAxis = (double)MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc).getScaledWidth() - this.getRenderX();
        GL11.glTranslated((double)(xAxis * percent), (double)0.0, (double)0.0);
lbl53:
        // 5 sources

        var6_5 = (String)this.modeValue.get();
        var7_6 = false;
        v3 = var6_5;
        if (v3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var6_5 = v3.toLowerCase();
        tmp = -1;
        switch (var6_5.hashCode()) {
            case -1454523186: {
                if (!var6_5.equals("newnovoline")) break;
                tmp = 1;
                break;
            }
            case 3020260: {
                if (!var6_5.equals("best")) break;
                tmp = 2;
                break;
            }
            case 1322963594: {
                if (!var6_5.equals("zamorozka")) break;
                tmp = 3;
                break;
            }
            case 1648341806: {
                if (!var6_5.equals("novoline")) break;
                tmp = 4;
                break;
            }
            case -441011516: {
                if (!var6_5.equals("novoline2")) break;
                tmp = 5;
                break;
            }
            case 3357441: {
                if (!var6_5.equals("moon")) break;
                tmp = 6;
                break;
            }
            case -1373558029: {
                if (!var6_5.equals("bzdhyp")) break;
                tmp = 7;
                break;
            }
            case -441011515: {
                if (!var6_5.equals("novoline3")) break;
                tmp = 8;
                break;
            }
            case -1102567108: {
                if (!var6_5.equals("liquid")) break;
                tmp = 9;
                break;
            }
            case -1133275996: {
                if (!var6_5.equals("sparklingwater")) break;
                tmp = 10;
                break;
            }
            case -1307030705: {
                if (!var6_5.equals("tenacity")) break;
                tmp = 11;
                break;
            }
            case -703561496: {
                if (!var6_5.equals("astolfo")) break;
                tmp = 12;
                break;
            }
            case 3146217: {
                if (!var6_5.equals("flux")) break;
                tmp = 13;
                break;
            }
            case 1973903356: {
                if (!var6_5.equals("watermelon")) break;
                tmp = 14;
                break;
            }
            case 3500745: {
                if (!var6_5.equals("rise")) break;
                tmp = 15;
                break;
            }
        }
        switch (tmp) {
            case 10: {
                v4 = this.prevTarget;
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawSparklingWater(v4, nowAnimHP);
                break;
            }
            case 4: {
                v5 = this.prevTarget;
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawNovo(v5, nowAnimHP);
                break;
            }
            case 12: {
                v6 = this.prevTarget;
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawAstolfo(v6, nowAnimHP);
                break;
            }
            case 9: {
                v7 = this.prevTarget;
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawLiquid(v7, nowAnimHP);
                break;
            }
            case 13: {
                v8 = this.prevTarget;
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawFlux(v8, nowAnimHP);
                break;
            }
            case 15: {
                v9 = this.prevTarget;
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawRise(v9, nowAnimHP);
                break;
            }
            case 2: {
                v10 = this.prevTarget;
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawBest(v10, nowAnimHP);
                break;
            }
            case 3: {
                v11 = this.prevTarget;
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawZamorozka(v11, nowAnimHP);
                break;
            }
            case 5: {
                v12 = this.prevTarget;
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawnovoline2(v12, nowAnimHP);
                break;
            }
            case 6: {
                v13 = this.prevTarget;
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawMoon(v13, nowAnimHP);
                break;
            }
            case 8: {
                v14 = this.prevTarget;
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawnovoline3(v14, nowAnimHP);
                break;
            }
            case 1: {
                v15 = this.prevTarget;
                if (v15 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawnewnovo(v15, nowAnimHP);
                break;
            }
            case 11: {
                v16 = this.prevTarget;
                if (v16 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawTenacity(v16, nowAnimHP);
                break;
            }
            case 14: {
                v17 = this.prevTarget;
                if (v17 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawWaterMelon(v17, nowAnimHP);
                break;
            }
            case 7: {
                v18 = this.prevTarget;
                if (v18 == null) {
                    Intrinsics.throwNpe();
                }
                this.drawBzdhyp(v18, nowAnimHP);
            }
        }
        return this.getTBorder();
    }

    private final void drawWaterMelon(IEntityLivingBase target, float easingHealth) {
        RenderUtils.drawRoundedCornerRect(-1.5f, 2.5f, 152.5f, 52.5f, 5.0f, new Color(0, 0, 0, 26).getRGB());
        RenderUtils.drawRoundedCornerRect(-1.0f, 2.0f, 152.0f, 52.0f, 5.0f, new Color(0, 0, 0, 26).getRGB());
        RenderUtils.drawRoundedCornerRect(-0.5f, 1.5f, 151.5f, 51.5f, 5.0f, new Color(0, 0, 0, 40).getRGB());
        RenderUtils.drawRoundedCornerRect(-0.0f, 1.0f, 151.0f, 51.0f, 5.0f, new Color(0, 0, 0, 60).getRGB());
        RenderUtils.drawRoundedCornerRect(0.5f, 0.5f, 150.5f, 50.5f, 5.0f, new Color(0, 0, 0, 50).getRGB());
        RenderUtils.drawRoundedCornerRect(1.0f, 0.0f, 150.0f, 50.0f, 5.0f, new Color(0, 0, 0, 50).getRGB());
        int hurtPercent = target.getHurtTime();
        float scale = hurtPercent == 0 ? 1.0f : ((float)hurtPercent < 0.5f ? 1.0f - 0.1f * (float)hurtPercent * (float)2 : 0.9f + 0.1f * ((float)hurtPercent - 0.5f) * (float)2);
        int size = 35;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)5.0f, (float)5.0f, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glTranslatef((float)((float)size * 0.5f * (1.0f - scale) / scale), (float)((float)size * 0.5f * (1.0f - scale) / scale), (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)(1 - hurtPercent), (float)(1 - hurtPercent), (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ITextureManager iTextureManager = MinecraftInstance.mc.getTextureManager();
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        iTextureManager.bindTexture(iNetworkPlayerInfo.getLocationSkin());
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 40.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
        GL11.glPopMatrix();
        Fonts.font35.drawString(String.valueOf(target.getName()), 45.0f, 12.0f, Color.WHITE.getRGB());
        DecimalFormat df = new DecimalFormat("0.00");
        Fonts.font30.drawString("Armor " + df.format(PlayerUtils.INSTANCE.getAr(target) * (double)100) + '%', 45.0f, 24.0f, new Color(200, 200, 200).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 32.0f, 145.0f, 42.0f, 5.0f, new Color(0, 0, 0, 100).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 32.0f, 45.0f + easingHealth / target.getMaxHealth() * 100.0f, 42.0f, 5.0f, ColorUtils.rainbow().getRGB());
        Fonts.font30.drawString(df.format(Float.valueOf(easingHealth / target.getMaxHealth() * (float)100)) + '%', 80.0f, 34.0f, new Color(255, 255, 255).getRGB(), true);
    }

    private final void drawBzdhyp(IEntityLivingBase target, float easingHealth) {
        RenderUtils.drawImage(MinecraftInstance.classProvider.createResourceLocation("fdpclient/shadow/shadow.png"), 0, 0, 237, 96);
        int hurtPercent = target.getHurtTime();
        float scale = hurtPercent == 0 ? 1.0f : ((float)hurtPercent < 0.5f ? 1.0f - 0.1f * (float)hurtPercent * (float)2 : 0.9f + 0.1f * ((float)hurtPercent - 0.5f) * (float)2);
        int size = 35;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)5.0f, (float)5.0f, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glTranslatef((float)((float)size * 0.5f * (1.0f - scale) / scale), (float)((float)size * 0.5f * (1.0f - scale) / scale), (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)(1 - hurtPercent), (float)(1 - hurtPercent), (float)1.0f);
        float scaleHT = RangesKt.coerceIn((float)((float)target.getHurtTime() / 10.0f), (float)0.0f, (float)1.0f);
        if (MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null) {
            INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
            if (iNetworkPlayerInfo == null) {
                Intrinsics.throwNpe();
            }
            this.drawHead(iNetworkPlayerInfo.getLocationSkin(), 5.0f + 15.0f * (scaleHT * 0.2f), 5.0f + 15.0f * (scaleHT * 0.2f), 1.0f - scaleHT * 0.2f, 30, 30, 1.0f, 0.4f + (1.0f - scaleHT) * 0.6f, 0.4f + (1.0f - scaleHT) * 0.6f, 1.0f);
        }
        GL11.glPopMatrix();
        DecimalFormat df = new DecimalFormat("0.00");
        Fonts.font30.drawString("Armor " + df.format(PlayerUtils.INSTANCE.getAr(target) * (double)100) + '%', 45.0f, 24.0f, new Color(200, 200, 200).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 32.0f, 145.0f, 42.0f, 5.0f, new Color(0, 0, 0, 100).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 32.0f, 45.0f + easingHealth / target.getMaxHealth() * 100.0f, 42.0f, 5.0f, ColorUtils.rainbow().getRGB());
        Fonts.font30.drawString(df.format(Float.valueOf(easingHealth / target.getMaxHealth() * (float)100)) + '%', 80.0f, 34.0f, new Color(255, 255, 255).getRGB(), true);
    }

    private final void drawSparklingWater(IEntityLivingBase target, float easingHealth) {
        RenderUtils.drawRoundedCornerRect(-1.5f, 2.5f, 152.5f, 52.5f, 5.0f, new Color(0, 0, 0, 26).getRGB());
        RenderUtils.drawRoundedCornerRect(-1.0f, 2.0f, 152.0f, 52.0f, 5.0f, new Color(0, 0, 0, 26).getRGB());
        RenderUtils.drawRoundedCornerRect(-0.5f, 1.5f, 151.5f, 51.5f, 5.0f, new Color(0, 0, 0, 40).getRGB());
        RenderUtils.drawRoundedCornerRect(-0.0f, 1.0f, 151.0f, 51.0f, 5.0f, new Color(0, 0, 0, 60).getRGB());
        RenderUtils.drawRoundedCornerRect(0.5f, 0.5f, 150.5f, 50.5f, 5.0f, new Color(0, 0, 0, 50).getRGB());
        RenderUtils.drawRoundedCornerRect(1.0f, 0.0f, 150.0f, 50.0f, 5.0f, new Color(0, 0, 0, 50).getRGB());
        if (target.getHurtTime() > 1) {
            GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.0f, (float)0.5f);
            RenderUtils.drawEntityOnScreen(25, 48, 32, target);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtils.drawEntityOnScreen(25, 45, 30, target);
        }
        Fonts.font35.drawString(String.valueOf(target.getName()), 45.0f, 6.0f, Color.WHITE.getRGB());
        DecimalFormat df = new DecimalFormat("0.00");
        Fonts.font30.drawString("Armor " + df.format(PlayerUtils.INSTANCE.getAr(target) * (double)100) + '%', 45.0f, 40.0f, new Color(200, 200, 200).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 23.0f, 145.0f, 33.0f, 5.0f, new Color(0, 0, 0, 100).getRGB());
        RenderUtils.drawRoundedCornerRect(45.0f, 23.0f, 45.0f + easingHealth / target.getMaxHealth() * 100.0f, 33.0f, 5.0f, ColorUtils.rainbow().getRGB());
        Fonts.font30.drawString(df.format(Float.valueOf(easingHealth / target.getMaxHealth() * (float)100)) + '%', 80.0f, 25.0f, new Color(255, 255, 255).getRGB(), true);
    }

    private final void drawAstolfo(IEntityLivingBase target, float nowAnimHP) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        Color color = RenderUtils.skyRainbow(1, 1.0f, 0.9f);
        float hpPct = nowAnimHP / target.getMaxHealth();
        RenderUtils.drawRect(0.0f, 0.0f, 140.0f, 60.0f, new Color(0, 0, 0, 110).getRGB());
        RenderUtils.drawRect(3.0f, 55.0f, 137.0f, 58.0f, ColorUtils.reAlpha(color, 100.0f).getRGB());
        RenderUtils.drawRect(3.0f, 55.0f, (float)3 + hpPct * 134.0f, 58.0f, color.getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawEntityOnScreen(18, 46, 20, target);
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        font.drawStringWithShadow(string, 37, 6, -1);
        GL11.glPushMatrix();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        font.drawString(MathKt.roundToInt((float)this.getHealth(target)) + " \u2764", 19, 9, color.getRGB());
        GL11.glPopMatrix();
    }

    private final void drawNovo(IEntityLivingBase target, float nowAnimHP) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        Color color = ColorUtils.healthColor$default(ColorUtils.INSTANCE, this.getHealth(target), target.getMaxHealth(), 0, 4, null);
        Color darkColor = ColorUtils.INSTANCE.darker(color, 0.6f);
        float hpPos = 33.0f + (float)(MathKt.roundToInt((float)(this.getHealth(target) / target.getMaxHealth() * (float)10000)) / 100);
        RenderUtils.drawRect(0.0f, 0.0f, 140.0f, 40.0f, new Color(40, 40, 40).getRGB());
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        font.drawString(string, 33, 5, Color.WHITE.getRGB());
        RenderUtils.drawEntityOnScreen(20, 35, 15, target);
        RenderUtils.drawRect(hpPos, 18.0f, 33.0f + (float)(MathKt.roundToInt((float)(nowAnimHP / target.getMaxHealth() * (float)10000)) / 100), 25.0f, darkColor);
        RenderUtils.drawRect(33.0f, 18.0f, hpPos, 25.0f, color);
        font.drawString("\u2764", 33, 30, Color.RED.getRGB());
        font.drawString(this.decimalFormat.format(Float.valueOf(this.getHealth(target))), 43, 30, Color.WHITE.getRGB());
    }

    public final void drawHead(IResourceLocation skin, int x, int y, int width, int height, float alpha) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        MinecraftInstance.mc.getTextureManager().bindTexture(skin);
        Gui.func_152125_a((int)x, (int)y, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static /* synthetic */ void drawHead$default(Target target, IResourceLocation iResourceLocation, int n, int n2, int n3, int n4, float f, int n5, Object object) {
        if ((n5 & 2) != 0) {
            n = 2;
        }
        if ((n5 & 4) != 0) {
            n2 = 2;
        }
        if ((n5 & 0x20) != 0) {
            f = 1.0f;
        }
        target.drawHead(iResourceLocation, n, n2, n3, n4, f);
    }

    public final void drawHead(IResourceLocation skin, float x, float y, float scale, int width, int height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)RangesKt.coerceIn((float)red, (float)0.0f, (float)1.0f), (float)RangesKt.coerceIn((float)green, (float)0.0f, (float)1.0f), (float)RangesKt.coerceIn((float)blue, (float)0.0f, (float)1.0f), (float)RangesKt.coerceIn((float)alpha, (float)0.0f, (float)1.0f));
        MinecraftInstance.mc.getTextureManager().bindTexture(skin);
        Gui.func_152125_a((int)0, (int)0, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static /* synthetic */ void drawHead$default(Target target, IResourceLocation iResourceLocation, float f, float f2, float f3, int n, int n2, float f4, float f5, float f6, float f7, int n3, Object object) {
        if ((n3 & 0x200) != 0) {
            f7 = 1.0f;
        }
        target.drawHead(iResourceLocation, f, f2, f3, n, n2, f4, f5, f6, f7);
    }

    private final void drawLiquid(IEntityLivingBase target, float easingHealth) {
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.font40.getStringWidth(string)), (int)118);
        RenderUtils.drawBorderedRect(0.0f, 0.0f, width, 36.0f, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
        if (easingHealth > this.getHealth(target)) {
            RenderUtils.drawRect(0.0f, 34.0f, easingHealth / target.getMaxHealth() * width, 36.0f, new Color(252, 185, 65).getRGB());
        }
        RenderUtils.drawRect(0.0f, 34.0f, this.getHealth(target) / target.getMaxHealth() * width, 36.0f, new Color(252, 96, 66).getRGB());
        if (easingHealth < this.getHealth(target)) {
            RenderUtils.drawRect(easingHealth / target.getMaxHealth() * width, 34.0f, this.getHealth(target) / target.getMaxHealth() * width, 36.0f, new Color(44, 201, 144).getRGB());
        }
        String string2 = target.getName();
        boolean bl = false;
        boolean bl2 = false;
        String it = string2;
        boolean bl3 = false;
        String string3 = it;
        if (string3 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font40.drawString(string3, 36, 3, 0xFFFFFF);
        StringBuilder stringBuilder = new StringBuilder().append("Distance: ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font35.drawString(stringBuilder.append(this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, target))).toString(), 36, 15, 0xFFFFFF);
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 2, 2, 30, 30);
        INetworkPlayerInfo playerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (playerInfo != null) {
            Fonts.font35.drawString("Ping: " + RangesKt.coerceAtLeast((int)playerInfo.getResponseTime(), (int)0), 36, 24, 0xFFFFFF);
        }
    }

    private final void drawZamorozka(IEntityLivingBase target, float easingHealth) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        RenderUtils.drawCircleRect(0.0f, 0.0f, 150.0f, 55.0f, 5.0f, new Color(0, 0, 0, 70).getRGB());
        RenderUtils.drawRect(7.0f, 7.0f, 35.0f, 40.0f, new Color(0, 0, 0, 70).getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawEntityOnScreen(21, 38, 15, target);
        float barLength = 136.0f;
        RenderUtils.drawCircleRect(7.0f, 45.0f, 143.0f, 50.0f, 2.5f, new Color(0, 0, 0, 70).getRGB());
        RenderUtils.drawCircleRect(7.0f, 45.0f, (float)7 + RangesKt.coerceAtLeast((float)(easingHealth / target.getMaxHealth() * barLength), (float)5.0f), 50.0f, 2.5f, ColorUtils.INSTANCE.rainbowWithAlpha(90).getRGB());
        RenderUtils.drawCircleRect(7.0f, 45.0f, (float)7 + RangesKt.coerceAtLeast((float)(target.getHealth() / target.getMaxHealth() * barLength), (float)5.0f), 50.0f, 2.5f, ColorUtils.rainbow().getRGB());
        RenderUtils.drawCircleRect(43.0f, 15.0f - (float)font.getFontHeight(), 143.0f, 17.0f, (float)(font.getFontHeight() + 1) * 0.45f, new Color(0, 0, 0, 70).getRGB());
        font.drawCenteredString(target.getName() + ' ' + (EntityUtils2.getPing(target.asEntityPlayer()) != -1 ? "\u00a7f" + EntityUtils2.getPing(target.asEntityPlayer()) + "ms" : ""), 93.0f, 16.0f - (float)font.getFontHeight(), ColorUtils.rainbow().getRGB(), false);
        font.drawString("Health: " + this.decimalFormat.format(Float.valueOf(easingHealth)) + " \u00a77/ " + this.decimalFormat.format(Float.valueOf(target.getMaxHealth())), 43, 11 + font.getFontHeight(), Color.WHITE.getRGB());
        StringBuilder stringBuilder = new StringBuilder().append("Distance: ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        font.drawString(stringBuilder.append(this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, target))).toString(), 43, 11 + font.getFontHeight() * 2, Color.WHITE.getRGB());
    }

    /*
     * WARNING - void declaration
     */
    private final void drawMoon(IEntityLivingBase target, float easingHealth) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        String hp = this.decimalFormat.format(Float.valueOf(easingHealth));
        int additionalWidth = RangesKt.coerceAtLeast((int)font.getStringWidth(target.getName() + "  " + hp + " hp"), (int)75);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        float yPos = (float)(5 + font.getFontHeight()) + 3.0f;
        int stopPos = (int)((float)5 + (float)(135 - font.getStringWidth(this.decimalFormat.format(Float.valueOf(target.getMaxHealth())))) * (easingHealth / target.getMaxHealth()));
        int n = 5;
        IntProgression intProgression = RangesKt.step((IntProgression)((IntProgression)new IntRange(n, stopPos)), (int)5);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        int n5 = n2;
        int n6 = n3;
        if (n4 >= 0 ? n5 <= n6 : n5 >= n6) {
            while (true) {
                void i;
                double x1 = RangesKt.coerceAtMost((int)(i + 5), (int)stopPos);
                RenderUtils.quickDrawGradientSideways((double)i - 5.0, 0.0, 45.0 + (double)additionalWidth - 1.0, 1.0, ColorUtils.hslRainbow$default((int)i, 0.0f, 0.0f, 10, 0, 22, null).getRGB(), ColorUtils.hslRainbow$default((int)x1, 0.0f, 0.0f, 0, 0, 22, null).getRGB());
                if (i == n3) break;
                i += n4;
            }
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(37.0f, yPos + (float)5, 37.0f + (float)additionalWidth, yPos + (float)13, new Color(0, 0, 0, 100).getRGB());
        if (target.getHealth() <= target.getMaxHealth()) {
            RenderUtils.drawCircleRect(37.0f, yPos + (float)5, 37.0f + (float)(MathKt.roundToInt((float)(easingHealth / target.getMaxHealth() * (float)8100)) / 100), yPos + (float)13, 3.0f, new Color(0, 255, 0).getRGB());
        }
        if (target.getHealth() < target.getMaxHealth() / (float)2) {
            RenderUtils.drawCircleRect(37.0f, yPos + (float)5, 37.0f + (float)(MathKt.roundToInt((float)(easingHealth / target.getMaxHealth() * (float)8100)) / 100), yPos + (float)13, 3.0f, new Color(255, 255, 0).getRGB());
        }
        if (target.getHealth() < target.getMaxHealth() / (float)4) {
            RenderUtils.drawCircleRect(37.0f, yPos + (float)5, 37.0f + (float)(MathKt.roundToInt((float)(easingHealth / target.getMaxHealth() * (float)8100)) / 100), yPos + (float)13, 3.0f, new Color(255, 0, 0).getRGB());
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        font.drawString(string, 37, 5, Color.WHITE.getRGB());
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 2, 2, 32, 32);
        GL11.glScaled((double)0.7, (double)0.7, (double)0.7);
        String string2 = hp + " hp";
        n3 = 0;
        n4 = 0;
        String it = string2;
        boolean bl = false;
        font.drawString(it, 53, 23, Color.LIGHT_GRAY.getRGB());
    }

    /*
     * WARNING - void declaration
     */
    private final void drawRise(IEntityLivingBase target, float easingHealth) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        RenderUtils.drawCircleRect(0.0f, 0.0f, 150.0f, 50.0f, 5.0f, new Color(0, 0, 0, 130).getRGB());
        int hurtPercent = target.getHurtTime();
        float scale = hurtPercent == 0 ? 1.0f : ((float)hurtPercent < 0.5f ? 1.0f - 0.2f * (float)hurtPercent * (float)2 : 0.8f + 0.2f * ((float)hurtPercent - 0.5f) * (float)2);
        int size = 30;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)5.0f, (float)5.0f, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glTranslatef((float)((float)size * 0.5f * (1.0f - scale) / scale), (float)((float)size * 0.5f * (1.0f - scale) / scale), (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)(1 - hurtPercent), (float)(1 - hurtPercent), (float)1.0f);
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.quickDrawHead(iNetworkPlayerInfo.getLocationSkin(), 0, 0, size, size);
        GL11.glPopMatrix();
        font.drawString("Name " + target.getName(), 40, 11, Color.WHITE.getRGB());
        StringBuilder stringBuilder = new StringBuilder().append("Distance ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        font.drawString(stringBuilder.append(this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, target))).append(" Hurt ").append(target.getHurtTime()).toString(), 40, 11 + font.getFontHeight(), Color.WHITE.getRGB());
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        drawRise.1 $fun$renderSideway$1 = drawRise.1.INSTANCE;
        int stopPos = (int)((float)5 + (float)(135 - font.getStringWidth(this.decimalFormat.format(Float.valueOf(target.getMaxHealth())))) * (easingHealth / target.getMaxHealth()));
        int n = 5;
        IntProgression intProgression = RangesKt.step((IntProgression)((IntProgression)new IntRange(n, stopPos)), (int)5);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        int n5 = n2;
        int n6 = n3;
        if (n4 >= 0 ? n5 <= n6 : n5 >= n6) {
            while (true) {
                void i;
                $fun$renderSideway$1.invoke((int)i, RangesKt.coerceAtMost((int)(i + 5), (int)stopPos));
                if (i == n3) break;
                i += n4;
            }
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        font.drawString(this.decimalFormat.format(Float.valueOf(easingHealth)), stopPos + 5, 43 - font.getFontHeight() / 2, Color.WHITE.getRGB());
    }

    private final void drawBest(IEntityLivingBase target, float easingHealth) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        float f = 60;
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float addedLen = f + (float)font.getStringWidth(string) * 1.6f;
        RenderUtils.drawRect(0.0f, 0.0f, addedLen, 47.0f, new Color(0, 0, 0, 120).getRGB());
        RenderUtils.drawRoundedCornerRect(0.0f, 0.0f, easingHealth / target.getMaxHealth() * addedLen, 47.0f, 3.0f, new Color(0, 0, 0, 90).getRGB());
        int hurtPercent = target.getHurtTime();
        float scale = hurtPercent == 0 ? 1.0f : ((float)hurtPercent < 0.5f ? 1.0f - 0.1f * (float)hurtPercent * (float)2 : 0.9f + 0.1f * ((float)hurtPercent - 0.5f) * (float)2);
        int size = 35;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)5.0f, (float)5.0f, (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)(1 - hurtPercent), (float)(1 - hurtPercent), (float)1.0f);
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 0, 0, size, size);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        font.drawString(string2, 39, 8, Color.WHITE.getRGB());
        GL11.glPopMatrix();
        font.drawString("Health " + MathKt.roundToInt((float)target.getHealth()), 56, 20 + (int)((double)font.getFontHeight() * 1.5), Color.WHITE.getRGB());
    }

    private final void drawFlux(IEntityLivingBase target, float nowAnimHP) {
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.font40.getStringWidth(string)), (int)70);
        RenderUtils.drawRect(0.0f, 0.0f, width, 34.0f, new Color(40, 40, 40).getRGB());
        RenderUtils.drawRect(2.0f, 22.0f, width - 2.0f, 24.0f, Color.BLACK.getRGB());
        RenderUtils.drawRect(2.0f, 28.0f, width - 2.0f, 30.0f, Color.BLACK.getRGB());
        RenderUtils.drawRect(2.0f, 22.0f, (float)2 + nowAnimHP / target.getMaxHealth() * (width - (float)4), 24.0f, new Color(231, 182, 0).getRGB());
        RenderUtils.drawRect(2.0f, 22.0f, (float)2 + this.getHealth(target) / target.getMaxHealth() * (width - (float)4), 24.0f, new Color(0, 224, 84).getRGB());
        RenderUtils.drawRect(2.0f, 28.0f, (float)2 + (float)target.getTotalArmorValue() / 20.0f * (width - (float)4), 30.0f, new Color(77, 128, 255).getRGB());
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font40.drawString(string2, 22, 3, Color.WHITE.getRGB());
        GL11.glPushMatrix();
        GL11.glScaled((double)0.7, (double)0.7, (double)0.7);
        Fonts.font35.drawString("Health: " + this.decimalFormat.format(Float.valueOf(this.getHealth(target))), 31.428572f, (float)(4 + Fonts.font40.getFontHeight()) / 0.7f, Color.WHITE.getRGB());
        GL11.glPopMatrix();
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 2, 2, 16, 16);
    }

    private final void drawnovoline2(IEntityLivingBase target, float easingHealth) {
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.font40.getStringWidth(string)), (int)118);
        RenderUtils.drawRect(0.0f, 0.0f, width + 14.0f, 44.0f, new Color(0, 0, 0, ((Number)this.backgroundalpha.get()).intValue()).getRGB());
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 3, 3, 30, 30);
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font40.drawString(string2, 34.5f, 4.0f, Color.WHITE.getRGB());
        Fonts.font40.drawString("Health: " + this.decimalFormat.format(Float.valueOf(target.getHealth())), 34.5f, 14.0f, Color.WHITE.getRGB());
        StringBuilder stringBuilder = new StringBuilder().append("Distance: ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font40.drawString(stringBuilder.append(this.decimalFormat.format(Float.valueOf(iEntityPlayerSP.getDistanceToEntity(target)))).append('m').toString(), 34.5f, 24.0f, Color.WHITE.getRGB());
        RenderUtils.drawRect(2.5f, 35.5f, width + 11.5f, 37.5f, new Color(0, 0, 0, 200).getRGB());
        RenderUtils.drawRect(3.0f, 36.0f, 3.0f + easingHealth / target.getMaxHealth() * (width + 8.0f), 37.0f, new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()).getRGB());
        RenderUtils.drawRect(2.5f, 39.5f, width + 11.5f, 41.5f, new Color(0, 0, 0, 200).getRGB());
        RenderUtils.drawRect(3.0f, 40.0f, 3.0f + (float)target.getTotalArmorValue() / 20.0f * (width + 8.0f), 41.0f, new Color(77, 128, 255).getRGB());
    }

    private final void drawnovoline3(IEntityLivingBase target, float easingHealth) {
        Color mainColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
        int percent = (int)target.getHealth();
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float nameLength = (float)RangesKt.coerceAtLeast((int)Fonts.font40.getStringWidth(string), (int)Fonts.font40.getStringWidth(String.valueOf(this.decimalFormat.format((Object)percent)))) + 20.0f;
        float barWidth = RangesKt.coerceIn((float)(target.getHealth() / target.getMaxHealth()), (float)0.0f, (float)target.getMaxHealth()) * (nameLength - 2.0f);
        RenderUtils.drawRect(-2.0f, -2.0f, 3.0f + nameLength + 36.0f, 38.0f, new Color(50, 50, 50, 150).getRGB());
        RenderUtils.drawRect(-1.0f, -1.0f, 2.0f + nameLength + 36.0f, 37.0f, new Color(0, 0, 0, 100).getRGB());
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 0, 0, 36, 36);
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.minecraftFont.drawStringWithShadow(string2, 38, 2, -1);
        RenderUtils.drawRect(37.0f, 14.0f, 37.0f + nameLength, 24.0f, new Color(0, 0, 0, 200).getRGB());
        float animateThingy = RangesKt.coerceIn((float)easingHealth, (float)target.getHealth(), (float)target.getMaxHealth()) / target.getMaxHealth() * (nameLength - 2.0f);
        if (easingHealth > target.getHealth()) {
            RenderUtils.drawRect(38.0f, 15.0f, 38.0f + animateThingy, 23.0f, mainColor.darker().getRGB());
        }
        RenderUtils.drawRect(38.0f, 15.0f, 38.0f + barWidth, 23.0f, mainColor.getRGB());
        Fonts.minecraftFont.drawStringWithShadow(String.valueOf(this.decimalFormat.format((Object)percent)), 38, 26, Color.WHITE.getRGB());
    }

    private final void drawnewnovo(IEntityLivingBase target, float easingHealth) {
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float width = RangesKt.coerceAtLeast((int)(38 + Fonts.minecraftFont.getStringWidth(string)), (int)118);
        this.counter1[0] = this.counter1[0] + 1;
        this.counter2[0] = this.counter2[0] + 1;
        this.counter1[0] = RangesKt.coerceIn((int)this.counter1[0], (int)0, (int)50);
        this.counter2[0] = RangesKt.coerceIn((int)this.counter2[0], (int)0, (int)80);
        RenderUtils.drawRect(0.0f, 0.0f, width, 34.5f, new Color(0, 0, 0, ((Number)this.backgroundalpha.get()).intValue()));
        Color customColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), 255);
        Color customColor1 = new Color(((Number)this.gredValue.get()).intValue(), ((Number)this.ggreenValue.get()).intValue(), ((Number)this.gblueValue.get()).intValue(), 255);
        RenderUtils.drawGradientSideways(34.0, 16.0, (double)width - (double)2, 24.0, new Color(40, 40, 40, 220).getRGB(), new Color(60, 60, 60, 255).getRGB());
        RenderUtils.drawGradientSideways(34.0, 16.0, (double)(36.0f + easingHealth / target.getMaxHealth() * (width - 36.0f)) - (double)2, 24.0, Palette.fade2(customColor, this.counter1[0], Fonts.font35.getFontHeight()).getRGB(), Palette.fade2(customColor1, this.counter2[0], Fonts.font35.getFontHeight()).getRGB());
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.minecraftFont.drawString(string2, 34, 4, new Color(255, 255, 255, 255).getRGB());
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        RenderUtils.drawHead(iNetworkPlayerInfo.getLocationSkin(), 2, 2, 30, 30);
        Fonts.minecraftFont.drawStringWithShadow(new BigDecimal(target.getHealth() / target.getMaxHealth() * (float)100).setScale(1, 4).toString() + "%", (int)(width / (float)2 + (float)5), 17, Color.white.getRGB());
    }

    private final void drawTenacity(IEntityLivingBase target, float easingHealth) {
        IFontRenderer font = (IFontRenderer)this.fontValue.get();
        String string = target.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        int additionalWidth = RangesKt.coerceAtLeast((int)font.getStringWidth(string), (int)75);
        int hurtPercent = target.getHurtTime();
        RenderUtils.drawRoundedCornerRect(0.0f, 5.0f, 59.0f + (float)additionalWidth, 45.0f, 6.0f, ColorUtils.rainbow().getRGB());
        float scale = hurtPercent == 0 ? 1.0f : ((float)hurtPercent < 0.5f ? 1.0f - 0.1f * (float)hurtPercent * (float)2 : 0.9f + 0.1f * ((float)hurtPercent - 0.5f) * (float)2);
        int size = 35;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)5.0f, (float)5.0f, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glTranslatef((float)((float)size * 0.5f * (1.0f - scale) / scale), (float)((float)size * 0.5f * (1.0f - scale) / scale), (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)(1 - hurtPercent), (float)(1 - hurtPercent), (float)1.0f);
        ITextureManager iTextureManager = MinecraftInstance.mc.getTextureManager();
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        iTextureManager.bindTexture(iNetworkPlayerInfo.getLocationSkin());
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
        RenderUtils.drawScaledCustomSizeModalCircle(5, 5, 40.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
        GL11.glPopMatrix();
        String string2 = target.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        font.drawCenteredString(string2, (float)45 + (float)additionalWidth / 2.0f, 1.0f + (float)font.getFontHeight(), Color.WHITE.getRGB(), false);
        StringBuilder stringBuilder = new StringBuilder().append(String.valueOf(MathKt.roundToInt((float)(easingHealth / target.getMaxHealth() * (float)100)))).append(" - ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        String infoStr = stringBuilder.append(String.valueOf(MathKt.roundToInt((double)PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, target)))).append("M").toString();
        font.drawString(infoStr, 45.0f + (float)(additionalWidth - font.getStringWidth(infoStr)) / 2.0f, 2.0f + (float)(font.getFontHeight() + font.getFontHeight()), Color.WHITE.getRGB(), false);
        RenderUtils.drawRoundedCornerRect(44.0f, 32.0f, 44.0f + (float)additionalWidth, 38.0f, 2.5f, new Color(60, 60, 60, 130).getRGB());
        RenderUtils.drawRoundedCornerRect(44.0f, 32.0f, 44.0f + easingHealth / target.getMaxHealth() * (float)additionalWidth, 38.0f, 2.5f, new Color(240, 240, 240, 250).getRGB());
    }

    private final Border getTBorder() {
        Border border;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "novoline": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            case "astolfo": {
                border = new Border(0.0f, 0.0f, 140.0f, 60.0f);
                break;
            }
            case "liquid": {
                float f = 38;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                String string3 = iEntityPlayerSP.getName();
                if (string3 == null) {
                    Intrinsics.throwNpe();
                }
                border = new Border(0.0f, 0.0f, f + (float)RangesKt.coerceAtLeast((int)Fonts.font40.getStringWidth(string3), (int)118), 36.0f);
                break;
            }
            case "flux": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                String string4 = iEntityPlayerSP.getName();
                if (string4 == null) {
                    Intrinsics.throwNpe();
                }
                border = new Border(0.0f, 0.0f, RangesKt.coerceAtLeast((int)(38 + Fonts.font40.getStringWidth(string4)), (int)70), 34.0f);
                break;
            }
            case "rise": {
                border = new Border(0.0f, 0.0f, 150.0f, 55.0f);
                break;
            }
            case "zamorozka": {
                border = new Border(0.0f, 0.0f, 150.0f, 55.0f);
                break;
            }
            case "exhibition": {
                border = new Border(0.0f, 0.0f, 140.0f, 45.0f);
                break;
            }
            case "best": {
                border = new Border(0.0f, 0.0f, 150.0f, 47.0f);
                break;
            }
            case "novoline2": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            case "novoline3": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            case "newnovoline": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            case "moon": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            case "bzdhyp": {
                border = new Border(0.0f, 0.0f, 120.0f, 48.0f);
                break;
            }
            case "watermelon": {
                border = new Border(0.0f, 0.0f, 120.0f, 48.0f);
                break;
            }
            case "sparklingwater": {
                border = new Border(0.0f, 0.0f, 120.0f, 48.0f);
                break;
            }
            case "tenacity": {
                border = new Border(0.0f, 0.0f, 140.0f, 40.0f);
                break;
            }
            default: {
                border = null;
            }
        }
        return border;
    }

    public Target() {
        super(-46.0, -40.0, 1.0f, new Side(Side.Horizontal.MIDDLE, Side.Vertical.MIDDLE));
    }
}

