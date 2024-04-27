/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.Regex
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import liying.utils.PotionData;
import liying.utils.Translate;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Effects")
public final class Effects
extends Element {
    private static final BoolValue blur;
    private float healthamin;
    private float easingwith;
    private float easinghealth;
    private final Map potionMap;
    public static final Companion Companion;
    private MSTimer timer;
    private float backamin;

    public final void setBackamin(float f) {
        this.backamin = f;
    }

    public final MSTimer getTimer() {
        return this.timer;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public final float getEasingwith() {
        return this.easingwith;
    }

    public Effects(double d, double d2, float f) {
        super(0.0, 0.0, 0.0f, null, 15, null);
        this.potionMap = new HashMap();
        this.timer = new MSTimer();
    }

    public final float getHealthamin() {
        return this.healthamin;
    }

    public final void updateAnimwith(float f) {
        Effects effects = this;
        float f2 = 2.0f;
        float f3 = 6.5f;
        float f4 = f - this.easingwith;
        float f5 = effects.easingwith;
        Effects effects2 = effects;
        boolean bl = false;
        float f6 = (float)Math.pow(f2, f3);
        effects2.easingwith = f5 + f4 / f6 * (float)RenderUtils.deltaTime;
        if (!this.timer.hasTimePassed(2L)) {
            return;
        }
        this.backamin += 1.0f;
        this.timer.reset();
    }

    public Effects(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 97.0;
        }
        if ((n & 2) != 0) {
            d2 = 141.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        this(d, d2, f);
    }

    public final void setHealthamin(float f) {
        this.healthamin = f;
    }

    public final float getBackamin() {
        return this.backamin;
    }

    public final void updateAnimhealth(float f) {
        Effects effects = this;
        float f2 = 2.0f;
        float f3 = 6.5f;
        float f4 = f - this.easinghealth;
        float f5 = effects.easinghealth;
        Effects effects2 = effects;
        boolean bl = false;
        float f6 = (float)Math.pow(f2, f3);
        effects2.easinghealth = f5 + f4 / f6 * (float)RenderUtils.deltaTime;
        if (!this.timer.hasTimePassed(2L)) {
            return;
        }
        this.healthamin += 1.0f;
        this.timer.reset();
    }

    static {
        Companion = new Companion(null);
        blur = new BoolValue("Blur", true);
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public Border drawElement() {
        GlStateManager.func_179094_E();
        var1_1 = 0.0f;
        var2_2 = 0.0f;
        var3_3 = 0.0f;
        var4_4 = 0;
        var5_5 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        var6_6 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        var7_7 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        var8_8 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        v0 = Objects.requireNonNull(MinecraftInstance.mc.getThePlayer());
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        for (IPotionEffect var9_10 : v0.getActivePotionEffects()) {
            var11_11 = Effects.access$getFunctions$p$s1046033730().getPotionById(var9_10.getPotionID());
            var12_12 = Effects.access$getFunctions$p$s1046033730().formatI18n(var11_11.getName(), new String[0]);
            var13_13 = null;
            if (!this.potionMap.containsKey(var11_11)) ** GOTO lbl-1000
            v1 = this.potionMap.get(var11_11);
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (((PotionData)v1).level == var9_10.getAmplifier()) {
                var13_13 = (PotionData)this.potionMap.get(var11_11);
            } else lbl-1000:
            // 2 sources

            {
                var14_15 = new PotionData(var11_11, new Translate(0.0f, -40.0f + (float)var4_4), var9_10.getAmplifier());
                var25_46 = var11_11;
                var24_45 = this.potionMap;
                var15_16 = 0;
                var16_18 = 0;
                var17_21 = var14_15;
                var18_24 = false;
                var13_13 = var17_21;
                var26_47 = var14_15;
                var24_45.put(var25_46, var26_47);
            }
            var14_14 = true;
            v2 = MinecraftInstance.mc.getThePlayer();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            for (IPotionEffect var15_17 : v2.getActivePotionEffects()) {
                v3 = var15_17.getAmplifier();
                v4 = var13_13;
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                if (v3 != v4.level) continue;
                var14_14 = false;
                break;
            }
            if (var14_14) {
                this.potionMap.remove(var11_11);
            }
            var15_16 = 0;
            var16_18 = 0;
            try {
                var17_21 = var9_10.getDurationString();
                var18_25 = ":";
                var19_28 = 0;
                var18_25 = new Regex(var18_25);
                var19_28 = 0;
                var20_36 = false;
                var17_21 = var18_25.split((CharSequence)var17_21, var19_28);
                var18_26 = false;
                if (!var17_21.isEmpty()) {
                    var19_29 = var17_21.listIterator(var17_21.size());
                    while (var19_29.hasPrevious()) {
                        var20_37 = (String)var19_29.previous();
                        var21_41 = false;
                        var22_42 = var20_37;
                        var23_44 = false;
                        if (var22_42.length() == 0) continue;
                        v5 = CollectionsKt.take((Iterable)((Iterable)var17_21), (int)(var19_29.nextIndex() + 1));
                        break;
                    }
                } else {
                    v5 = CollectionsKt.emptyList();
                }
                var17_21 = v5;
                var18_26 = false;
                var19_31 = var17_21;
                v6 = var19_31.toArray(new String[0]);
                if (v6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                var17_21 = v6[0];
                var18_26 = false;
                var15_16 = Integer.parseInt((String)var17_21);
                var17_21 = var9_10.getDurationString();
                var18_27 = ":";
                var19_32 = 0;
                var18_27 = new Regex(var18_27);
                var19_32 = 0;
                var20_38 = false;
                var17_21 = var18_27.split((CharSequence)var17_21, var19_32);
                var18_24 = false;
                if (!var17_21.isEmpty()) {
                    var19_33 = var17_21.listIterator(var17_21.size());
                    while (var19_33.hasPrevious()) {
                        var20_39 = (String)var19_33.previous();
                        var21_41 = false;
                        var22_42 = var20_39;
                        var23_44 = false;
                        if (var22_42.length() == 0) continue;
                        v7 = CollectionsKt.take((Iterable)((Iterable)var17_21), (int)(var19_33.nextIndex() + 1));
                        break;
                    }
                } else {
                    v7 = CollectionsKt.emptyList();
                }
                var17_21 = v7;
                var18_24 = false;
                var19_34 = var17_21;
                v8 = var19_34.toArray(new String[0]);
                if (v8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                var17_21 = v8[1];
                var18_24 = false;
                var16_18 = Integer.parseInt((String)var17_21);
            }
            catch (Exception var17_22) {
                var15_16 = 100;
                var16_18 = 1000;
            }
            var17_20 = var15_16 * 60 + var16_18;
            v9 = var13_13;
            if (v9 == null) {
                Intrinsics.throwNpe();
            }
            if (v9.getMaxTimer() == 0 || (double)var17_20 > (double)var13_13.getMaxTimer()) {
                var13_13.maxTimer = var17_20;
            }
            var18_23 = 0.0f;
            if ((double)var17_20 >= 0.0) {
                var18_23 = (float)((double)var17_20 / (double)var13_13.getMaxTimer() * 100.0);
            }
            var19_28 = Math.round(var13_13.translate.getY() / 1.4f - 7.22f);
            var18_23 = Math.max(var18_23, 2.0f);
            var13_13.translate.interpolate(0.0f, var4_4, 0.1);
            var13_13.animationX = (float)RenderUtils.getAnimationState2(var13_13.getAnimationX(), 1.2f * var18_23, Math.max(10.0f, Math.abs(var13_13.animationX - 1.2f * var18_23) * 15.0f) * 0.3f);
            var20_35 = Fonts.font35.getStringWidth(var12_12 + " " + this.intToRomanByGreedy(var9_10.getAmplifier() + 1));
            var1_1 = Fonts.font35.getStringWidth(var12_12 + " " + this.intToRomanByGreedy(var9_10.getAmplifier() + 1));
            if (var20_35 > var2_2) {
                var2_2 = var20_35;
            }
            var21_40 = var13_13.translate.getY() / 2.5f;
            var3_3 = var13_13.translate.getY() / 2.5f - 8.0f;
            Fonts.font35.drawString(var12_12 + " " + this.intToRomanByGreedy(var9_10.getAmplifier() + 1), 29.0f, -(var21_40 - (float)MinecraftInstance.mc.getFontRendererObj().getFontHeight()), -1);
            Fonts.font35.drawString(var9_10.getDurationString(), var1_1 + (float)32, -(var21_40 - 9.76f), -1);
            if (var11_11.getHasStatusIcon()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                var22_43 = var11_11.getStatusIconIndex();
                MinecraftInstance.mc.getTextureManager().bindTexture(MinecraftInstance.classProvider.createResourceLocation("textures/gui/container/inventory.png"));
                GL11.glPushMatrix();
                GL11.glScaled((double)0.55, (double)0.55, (double)1.0);
                MinecraftInstance.mc2.field_71456_v.func_175174_a(30.0f, -((float)var19_28 + 15.85f + 7.07f), var22_43 % 8 * 18, 198 + var22_43 / 8 * 18, 18, 18);
                GL11.glPopMatrix();
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)2929);
                GlStateManager.func_179121_F();
            }
            var4_4 -= 35;
        }
        this.updateAnimwith(var2_2 * 0.98f + 42.68f);
        this.updateAnimhealth(-var3_3 * 1.1f + 14.63f);
        RoundedUtil.drawGradientRound(12.2f, -7.32f, this.easingwith, this.easinghealth, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(var8_8, 0.85f), var5_5, var7_7, var6_6);
        GlStateManager.func_179121_F();
        FontLoaders.F18.drawStringWithShadow("Effects", 17.07, -3.66, new Color(255, 255, 255, 255).getRGB());
        return new Border(0.0f, 0.0f, 120.0f, 30.0f);
    }

    public final void setEasingwith(float f) {
        this.easingwith = f;
    }

    public Effects() {
        this(0.0, 0.0, 0.0f, 7, null);
    }

    public final void setEasinghealth(float f) {
        this.easinghealth = f;
    }

    public final float getEasinghealth() {
        return this.easinghealth;
    }

    private final String intToRomanByGreedy(int n) {
        int n2 = n;
        int[] nArray = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] stringArray = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nArray.length && n2 >= 0; ++i) {
            while (nArray[i] <= n2) {
                n2 -= nArray[i];
                stringBuilder.append(stringArray[i]);
            }
        }
        return stringBuilder.toString();
    }

    public final void setTimer(MSTimer mSTimer) {
        this.timer = mSTimer;
    }

    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

