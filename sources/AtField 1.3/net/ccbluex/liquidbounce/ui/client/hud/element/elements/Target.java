/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import liying.utils.blur.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.blur.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Target")
public final class Target
extends Element {
    private final FloatValue fadeSpeed;
    private IFontRenderer fontRenderer;
    private final BoolValue outline;
    private final BoolValue blur;
    private final BoolValue shadow;
    private final ListValue modeValue = new ListValue("Style", new String[]{"Novoline", "LiquidWing", "LiquidBounce"}, "Novoline");
    private final List riseParticleList;
    private IEntityLivingBase mainTarget;
    private float easingHealth;
    private final DecimalFormat decimalFormat;
    private float animProgress;

    public final float getAnimProgress() {
        return this.animProgress;
    }

    public final void setFontRenderer(IFontRenderer iFontRenderer) {
        this.fontRenderer = iFontRenderer;
    }

    private final void novoline(IEntityLivingBase iEntityLivingBase, float f, float f2) {
        block10: {
            block12: {
                block11: {
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
                    if (module == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
                    }
                    HUD hUD = (HUD)module;
                    if (iEntityLivingBase == null) break block10;
                    if (this.easingHealth < 0.0f || this.easingHealth > iEntityLivingBase.getMaxHealth()) break block11;
                    float f3 = this.easingHealth - iEntityLivingBase.getHealth();
                    boolean bl = false;
                    if (!((double)Math.abs(f3) < 0.01)) break block12;
                }
                this.easingHealth = iEntityLivingBase.getHealth();
            }
            Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue());
            Color color2 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue());
            RenderUtils.rectangleBordered(0.0, 0.0, f, f2, 0.5, new Color(0, 0, 0, 30).getRGB(), (Boolean)this.outline.get() != false ? RenderUtils.reAlpha(color.getRGB(), 0.4f) : new Color(0, 0, 0, 80).getRGB());
            if (((Boolean)this.shadow.get()).booleanValue()) {
                RenderUtils.drawShadowWithCustomAlpha(0.0f, 0.0f, (float)((double)f), (float)((double)f2), 255.0f);
            }
            if (((Boolean)this.blur.get()).booleanValue()) {
                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                BlurBuffer.blurArea((float)(this.getRenderX() + (double)0.0f), (float)(this.getRenderY() + (double)0.0f), (float)((double)f), (float)((double)f2));
                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            }
            RenderUtils.rectangle(36.0, 17.0, (double)f - (double)4, 27.0, new Color(35, 35, 35, 20).getRGB());
            RenderUtils.drawGradientSideways(36.0, 17.0, 36.0 + (double)(this.easingHealth / iEntityLivingBase.getMaxHealth() * (f - (float)40)), 27.0, color2.getRGB(), color.getRGB());
            this.fontRenderer.drawCenteredString(String.valueOf((float)((int)((double)(iEntityLivingBase.getHealth() / iEntityLivingBase.getMaxHealth()) * 1000.0)) / 10.0f) + "%", 36.0f + (f - (float)40) / 2.0f, 19.0f, -1, true);
            Target target = this;
            float f4 = 2.0f;
            float f5 = 10.0f - ((Number)this.fadeSpeed.get()).floatValue();
            float f6 = iEntityLivingBase.getHealth() - this.easingHealth;
            float f7 = target.easingHealth;
            Target target2 = target;
            boolean bl = false;
            float f8 = (float)Math.pow(f4, f5);
            target2.easingHealth = f7 + f6 / f8 * (float)RenderUtils.deltaTime;
            String string = iEntityLivingBase.getName();
            if (string != null) {
                String string2 = string;
                boolean bl2 = false;
                bl = false;
                String string3 = string2;
                boolean bl3 = false;
                this.fontRenderer.drawStringWithShadow(string3, 36, 5, 0xFFFFFF);
            }
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            INetworkPlayerInfo iNetworkPlayerInfo = iINetHandlerPlayClient.getPlayerInfo(iEntityPlayerSP.getUniqueID());
            if (MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase)) {
                iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(iEntityLivingBase.getUniqueID());
            }
            if (iNetworkPlayerInfo != null) {
                IResourceLocation iResourceLocation = iNetworkPlayerInfo.getLocationSkin();
                float f9 = (float)iEntityLivingBase.getHurtTime() - (iEntityLivingBase.getHurtTime() != 0 ? Minecraft.func_71410_x().field_71428_T.field_194147_b : 0.0f);
                float f10 = f9 / 10.0f;
                GL11.glColor4f((float)1.0f, (float)(1.0f - f10), (float)(1.0f - f10), (float)1.0f);
                float f11 = f10 == 0.0f ? 1.0f : (f10 < 0.5f ? 1.0f - 0.2f * f10 * (float)2 : 0.8f + 0.2f * (f10 - 0.5f) * (float)2);
                int n = 30;
                GL11.glPushMatrix();
                GL11.glScalef((float)f11, (float)f11, (float)f11);
                GL11.glTranslatef((float)((float)n * 0.5f * (1.0f - f11) / f11), (float)((float)n * 0.5f * (1.0f - f11) / f11), (float)0.0f);
                MinecraftInstance.mc.getTextureManager().bindTexture(iResourceLocation);
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, n, n, 64.0f, 64.0f);
                GL11.glPopMatrix();
            }
        }
    }

    public final IEntityLivingBase getMainTarget() {
        return this.mainTarget;
    }

    public Target() {
        super(0.0, 0.0, 0.0f, null, 15, null);
        List list;
        this.blur = new BoolValue("Blur", false);
        this.outline = new BoolValue("Outline", false);
        this.shadow = new BoolValue("Shadow", true);
        this.decimalFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        this.fadeSpeed = new FloatValue("FadeSpeed", 2.0f, 1.0f, 9.0f);
        this.fontRenderer = Fonts.productSans40;
        Target target = this;
        boolean bl = false;
        target.riseParticleList = list = (List)new ArrayList();
    }

    public final void setAnimProgress(float f) {
        this.animProgress = f;
    }

    private final void liquidwing(IEntityLivingBase iEntityLivingBase) {
        String string = iEntityLivingBase.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        float f = 26.0f + (float)RangesKt.coerceAtLeast((int)Fonts.font35.getStringWidth(string), (int)70);
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        RoundedUtil.drawGradientRound(0.0f, 0.0f, f, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
        if (this.easingHealth > iEntityLivingBase.getHealth()) {
            RoundedUtil.drawGradientRound(0.0f, 0.0f, f * (iEntityLivingBase.getHealth() / iEntityLivingBase.getMaxHealth()), 28.0f, 4.5f, ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
        }
        float f2 = (float)iEntityLivingBase.getHurtTime() / 10.0f;
        INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(iEntityLivingBase.getUniqueID());
        if (iNetworkPlayerInfo != null) {
            this.drawNarleboneHead(iNetworkPlayerInfo.getLocationSkin(), 24, 24, f2);
        }
        String string2 = iEntityLivingBase.getName();
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        Fonts.font30.drawString(string2, 32.0f, 7.0f, Color.WHITE.getRGB(), true);
        Fonts.font30.drawString("HP: " + this.decimalFormat.format(Float.valueOf(iEntityLivingBase.getHealth())), 32.0f, 17.0f, Color.WHITE.getRGB(), true);
        Target target = this;
        float f3 = 2.0f;
        float f4 = 10.0f - ((Number)this.fadeSpeed.get()).floatValue();
        float f5 = iEntityLivingBase.getHealth() - this.easingHealth;
        float f6 = target.easingHealth;
        Target target2 = target;
        boolean bl = false;
        float f7 = (float)Math.pow(f3, f4);
        target2.easingHealth = f6 + f5 / f7 * (float)RenderUtils.deltaTime;
        this.setBorder(new Border(0.0f, 0.0f, f, 28.0f));
    }

    private final void liquidbounce(IEntityLivingBase iEntityLivingBase, float f, float f2) {
        block11: {
            block10: {
                if (this.easingHealth < 0.0f || this.easingHealth > iEntityLivingBase.getMaxHealth()) break block10;
                float f3 = this.easingHealth - iEntityLivingBase.getHealth();
                boolean bl = false;
                if (!((double)Math.abs(f3) < 0.01)) break block11;
            }
            this.easingHealth = iEntityLivingBase.getHealth();
        }
        if (((Boolean)this.blur.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glPushMatrix();
            BlurUtils.blurArea((float)this.getRenderX(), (float)this.getRenderY(), f, f2);
            GL11.glPopMatrix();
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        RenderUtils.rectangle(0.0, 0.0, f, f2, new Color(0, 0, 0, 70).getRGB());
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hUD = (HUD)module;
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue());
        double d = 0.0;
        double d2 = f;
        double cfr_ignored_0 = -d * 2.0;
        RenderUtils.drawGradientSideways(d, (double)f2 - 2.0, d + (double)(this.easingHealth / iEntityLivingBase.getMaxHealth()) * d2, f2, color2.getRGB(), color.getRGB());
        Target target = this;
        float f4 = 2.0f;
        float f5 = 10.0f - ((Number)this.fadeSpeed.get()).floatValue();
        float f6 = iEntityLivingBase.getHealth() - this.easingHealth;
        float f7 = target.easingHealth;
        Target target2 = target;
        boolean bl = false;
        float f8 = (float)Math.pow(f4, f5);
        target2.easingHealth = f7 + f6 / f8 * (float)RenderUtils.deltaTime;
        String string = iEntityLivingBase.getName();
        if (string != null) {
            String string2 = string;
            boolean bl2 = false;
            bl = false;
            String string3 = string2;
            boolean bl3 = false;
            this.fontRenderer.drawStringWithShadow(string3, 36, 7, 0xFFFFFF);
        }
        StringBuilder stringBuilder = new StringBuilder().append("Distance: ");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        this.fontRenderer.drawStringWithShadow(stringBuilder.append(this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntityLivingBase))).toString(), 36, 19, 0xFFFFFF);
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        INetworkPlayerInfo iNetworkPlayerInfo = iINetHandlerPlayClient.getPlayerInfo(iEntityPlayerSP2.getUniqueID());
        if (MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase)) {
            iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(iEntityLivingBase.getUniqueID());
        }
        if (iNetworkPlayerInfo != null) {
            IResourceLocation iResourceLocation = iNetworkPlayerInfo.getLocationSkin();
            float f9 = (float)iEntityLivingBase.getHurtTime() - (iEntityLivingBase.getHurtTime() != 0 ? Minecraft.func_71410_x().field_71428_T.field_194147_b : 0.0f);
            float f10 = f9 / 10.0f;
            GL11.glColor4f((float)1.0f, (float)(1.0f - f10), (float)(1.0f - f10), (float)1.0f);
            float f11 = f10 == 0.0f ? 1.0f : (f10 < 0.5f ? 1.0f - 0.2f * f10 * (float)2 : 0.8f + 0.2f * (f10 - 0.5f) * (float)2);
            int n = 30;
            GL11.glPushMatrix();
            GL11.glScalef((float)f11, (float)f11, (float)f11);
            GL11.glTranslatef((float)((float)n * 0.5f * (1.0f - f11) / f11), (float)((float)n * 0.5f * (1.0f - f11) / f11), (float)0.0f);
            MinecraftInstance.mc.getTextureManager().bindTexture(iResourceLocation);
            RenderUtils.drawScaledCustomSizeModalRect(3, 5, 8.0f, 8.0f, 8, 8, n, n, 64.0f, 64.0f);
            GL11.glPopMatrix();
        }
    }

    public final IFontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    private final void drawNarleboneHead(IResourceLocation iResourceLocation, int n, int n2, float f) {
        GL11.glColor4f((float)1.0f, (float)(1.0f - f), (float)(1.0f - f), (float)1.0f);
        MinecraftInstance.mc.getTextureManager().bindTexture(iResourceLocation);
        Gui.func_152125_a((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)n, (int)n2, (float)64.0f, (float)64.0f);
    }

    public final void setMainTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.mainTarget = iEntityLivingBase;
    }

    @Override
    public Border drawElement() {
        boolean bl;
        String string;
        boolean bl2;
        this.fontRenderer = Fonts.productSans40;
        IEntityLivingBase iEntityLivingBase = LiquidBounce.INSTANCE.getCombatManager().getTarget();
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            iEntityLivingBase = MinecraftInstance.mc.getThePlayer();
        }
        if (MinecraftInstance.classProvider.isGuiChat(MinecraftInstance.mc.getCurrentScreen())) {
            iEntityLivingBase = MinecraftInstance.mc.getThePlayer();
        }
        float f = 0.0f;
        String string2 = (String)this.modeValue.get();
        boolean bl3 = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        f = string3.toLowerCase().equals("liquidwing") ? 150.0f : 128.0f;
        string2 = (String)this.modeValue.get();
        bl3 = false;
        String string4 = string2;
        if (string4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (!string4.toLowerCase().equals("novoline")) {
            // empty if block
        }
        String string5 = (String)this.modeValue.get();
        boolean bl4 = false;
        String string6 = string5;
        if (string6 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        float f2 = string6.toLowerCase().equals("liquidwing") ? 40.0f : 36.0f;
        string5 = (String)this.modeValue.get();
        bl4 = false;
        String string7 = string5;
        if (string7 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (!string7.toLowerCase().equals("novoline")) {
            // empty if block
        }
        float f3 = iEntityLivingBase != null ? 0.0f : 1.0f;
        Target target = this;
        float f4 = 2.0f;
        float f5 = 4.5f;
        float f6 = f3 - this.animProgress;
        float f7 = target.animProgress;
        Target target2 = target;
        boolean bl5 = false;
        float f8 = (float)Math.pow(f4, f5);
        target2.animProgress = f7 + f6 / f8 * (float)RenderUtils.deltaTime;
        this.animProgress = RangesKt.coerceIn((float)this.animProgress, (float)0.0f, (float)1.0f);
        if (iEntityLivingBase != null) {
            this.mainTarget = iEntityLivingBase;
        } else if (this.animProgress >= 1.0f) {
            this.mainTarget = null;
        }
        if (this.mainTarget == null) {
            this.easingHealth = 0.0f;
            return new Border(0.0f, 0.0f, f, f2);
        }
        String string8 = (String)this.modeValue.get();
        boolean bl6 = false;
        String string9 = string8;
        if (string9 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (string9.toLowerCase().equals("novoline")) {
            int n;
            int n2 = 38;
            IEntityLivingBase iEntityLivingBase2 = this.mainTarget;
            if (iEntityLivingBase2 == null) {
                Intrinsics.throwNpe();
            }
            String string10 = iEntityLivingBase2.getName();
            if (string10 != null) {
                string8 = string10;
                IFontRenderer iFontRenderer = MinecraftInstance.mc.getFontRendererObj();
                int n3 = n2;
                bl5 = false;
                bl2 = false;
                string = string8;
                bl = false;
                int n4 = iFontRenderer.getStringWidth(string);
                n2 = n3;
                n = n4;
            } else {
                n = 0;
            }
            f = RangesKt.coerceAtLeast((int)(n2 + n), (int)80);
        } else {
            string8 = (String)this.modeValue.get();
            bl6 = false;
            String string11 = string8;
            if (string11 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            if (string11.toLowerCase().equals("liquidwing")) {
                int n;
                int n5 = 38;
                IEntityLivingBase iEntityLivingBase3 = this.mainTarget;
                if (iEntityLivingBase3 == null) {
                    Intrinsics.throwNpe();
                }
                String string12 = iEntityLivingBase3.getName();
                if (string12 != null) {
                    string8 = string12;
                    IFontRenderer iFontRenderer = MinecraftInstance.mc.getFontRendererObj();
                    int n6 = n5;
                    bl5 = false;
                    bl2 = false;
                    string = string8;
                    bl = false;
                    int n7 = iFontRenderer.getStringWidth(string);
                    n5 = n6;
                    n = n7;
                } else {
                    n = 0;
                }
                f = RangesKt.coerceAtLeast((int)(n5 + n), (int)80);
            } else {
                int n;
                int n8 = 38;
                IEntityLivingBase iEntityLivingBase4 = this.mainTarget;
                if (iEntityLivingBase4 == null) {
                    Intrinsics.throwNpe();
                }
                String string13 = iEntityLivingBase4.getName();
                if (string13 != null) {
                    string8 = string13;
                    IFontRenderer iFontRenderer = MinecraftInstance.mc.getFontRendererObj();
                    int n9 = n8;
                    bl5 = false;
                    bl2 = false;
                    string = string8;
                    bl = false;
                    int n10 = iFontRenderer.getStringWidth(string);
                    n8 = n9;
                    n = n10;
                } else {
                    n = 0;
                }
                f = RangesKt.coerceAtLeast((int)(n8 + n), (int)100);
            }
        }
        float f9 = this.animProgress;
        float f10 = this.animProgress;
        float f11 = f / 2.0f * f9;
        float f12 = f2 / 2.0f * f10;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)f11, (float)f12, (float)0.0f);
        GL11.glScalef((float)(1.0f - f9), (float)(1.0f - f10), (float)(1.0f - f9));
        string = (String)this.modeValue.get();
        bl = false;
        String string14 = string;
        if (string14 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (string14.toLowerCase().equals("novoline")) {
            IEntityLivingBase iEntityLivingBase5 = this.mainTarget;
            if (iEntityLivingBase5 == null) {
                Intrinsics.throwNpe();
            }
            this.novoline(iEntityLivingBase5, f, f2);
        } else {
            string = (String)this.modeValue.get();
            bl = false;
            String string15 = string;
            if (string15 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            if (string15.toLowerCase().equals("liquidwing")) {
                IEntityLivingBase iEntityLivingBase6 = this.mainTarget;
                if (iEntityLivingBase6 == null) {
                    Intrinsics.throwNpe();
                }
                this.liquidwing(iEntityLivingBase6);
            } else {
                IEntityLivingBase iEntityLivingBase7 = this.mainTarget;
                if (iEntityLivingBase7 == null) {
                    Intrinsics.throwNpe();
                }
                this.liquidbounce(iEntityLivingBase7, f, f2);
            }
        }
        GL11.glPopMatrix();
        GlStateManager.func_179117_G();
        return new Border(0.0f, 0.0f, f, f2);
    }

    public static final class RiseParticle {
        private final long time;
        private final int x;
        private final int alpha;
        private final Color color = ColorUtils.rainbow(RandomUtils.nextInt(0, 30));
        private final int y;

        public RiseParticle() {
            this.alpha = RandomUtils.nextInt(150, 255);
            this.time = System.currentTimeMillis();
            this.x = RandomUtils.nextInt(-50, 50);
            this.y = RandomUtils.nextInt(-50, 50);
        }

        public final int getAlpha() {
            return this.alpha;
        }

        public final Color getColor() {
            return this.color;
        }

        public final long getTime() {
            return this.time;
        }

        public final int getY() {
            return this.y;
        }

        public final int getX() {
            return this.x;
        }
    }
}

