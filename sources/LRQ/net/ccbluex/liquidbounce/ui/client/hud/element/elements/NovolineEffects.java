/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
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
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.PotionData;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Translate;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="NovolineEffects")
public final class NovolineEffects
extends Element {
    private final Map<IPotion, PotionData> potionMap = new HashMap();
    private final FloatValue radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);

    /*
     * Unable to fully structure code
     */
    @Override
    public Border drawElement() {
        GlStateManager.func_179094_E();
        y = 0;
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        for (IPotionEffect potionEffect : v0.getActivePotionEffects()) {
            potion = MinecraftInstance.functions.getPotionById(potionEffect.getPotionID());
            name = MinecraftInstance.functions.formatI18n(potion.getName(), new String[0]);
            potionData = null;
            x2 = (float)((double)Fonts.fontSFUI35.getStringWidth(name + " " + this.intToRomanByGreedy(potionEffect.getAmplifier() + 1)) * 1.75);
            if (!this.potionMap.containsKey(potion)) ** GOTO lbl-1000
            v1 = this.potionMap.get(potion);
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (v1.level == potionEffect.getAmplifier()) {
                potionData = this.potionMap.get(potion);
            } else lbl-1000:
            // 2 sources

            {
                var8_9 = new PotionData(potion, new Translate(0.0f, -40.0f + (float)y), potionEffect.getAmplifier());
                var19_38 = potion;
                var18_37 = this.potionMap;
                var9_10 = false;
                var10_12 = false;
                it = var8_9;
                $i$a$-also-NovolineEffects$drawElement$1 = false;
                potionData = it;
                var20_39 = var8_9;
                var18_37.put(var19_38, var20_39);
            }
            flag = true;
            v2 = MinecraftInstance.mc.getThePlayer();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            for (IPotionEffect checkEffect : v2.getActivePotionEffects()) {
                v3 = checkEffect.getAmplifier();
                v4 = potionData;
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                if (v3 != v4.level) continue;
                flag = false;
                break;
            }
            if (flag) {
                this.potionMap.remove(potion);
            }
            potionTime = 0;
            potionMaxTime = 0;
            try {
                it = potionEffect.getDurationString();
                $i$a$-also-NovolineEffects$drawElement$1 = ":";
                var13_22 = 0;
                $i$a$-also-NovolineEffects$drawElement$1 = new Regex($i$a$-also-NovolineEffects$drawElement$1);
                var13_22 = 0;
                var14_30 = false;
                $this$dropLastWhile$iv = $i$a$-also-NovolineEffects$drawElement$1.split((CharSequence)it, var13_22);
                $i$f$dropLastWhile = false;
                if (!$this$dropLastWhile$iv.isEmpty()) {
                    iterator$iv = $this$dropLastWhile$iv.listIterator($this$dropLastWhile$iv.size());
                    while (iterator$iv.hasPrevious()) {
                        it = (String)iterator$iv.previous();
                        $i$a$-dropLastWhile-NovolineEffects$drawElement$2 = false;
                        var16_35 = it;
                        var17_36 = false;
                        if (var16_35.length() == 0) continue;
                        v5 = CollectionsKt.take((Iterable)$this$dropLastWhile$iv, (int)(iterator$iv.nextIndex() + 1));
                        break;
                    }
                } else {
                    v5 = CollectionsKt.emptyList();
                }
                $this$toTypedArray$iv = v5;
                $i$f$toTypedArray = false;
                thisCollection$iv = $this$toTypedArray$iv;
                v6 = thisCollection$iv.toArray(new String[0]);
                if (v6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                $this$toTypedArray$iv = v6[0];
                $i$f$toTypedArray = false;
                potionTime = Integer.parseInt((String)$this$toTypedArray$iv);
                $this$toTypedArray$iv = potionEffect.getDurationString();
                $i$f$toTypedArray = ":";
                thisCollection$iv = 0;
                $i$f$toTypedArray = new Regex($i$f$toTypedArray);
                thisCollection$iv = 0;
                it = false;
                $this$dropLastWhile$iv = $i$f$toTypedArray.split((CharSequence)$this$toTypedArray$iv, thisCollection$iv);
                $i$f$dropLastWhile = false;
                if (!$this$dropLastWhile$iv.isEmpty()) {
                    iterator$iv = $this$dropLastWhile$iv.listIterator($this$dropLastWhile$iv.size());
                    while (iterator$iv.hasPrevious()) {
                        it = (String)iterator$iv.previous();
                        $i$a$-dropLastWhile-NovolineEffects$drawElement$3 = false;
                        var16_35 = it;
                        var17_36 = false;
                        if (var16_35.length() == 0) continue;
                        v7 = CollectionsKt.take((Iterable)$this$dropLastWhile$iv, (int)(iterator$iv.nextIndex() + 1));
                        break;
                    }
                } else {
                    v7 = CollectionsKt.emptyList();
                }
                $this$toTypedArray$iv = v7;
                $i$f$toTypedArray = false;
                thisCollection$iv = $this$toTypedArray$iv;
                v8 = thisCollection$iv.toArray(new String[0]);
                if (v8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                $this$toTypedArray$iv = v8[1];
                $i$f$toTypedArray = false;
                potionMaxTime = Integer.parseInt((String)$this$toTypedArray$iv);
            }
            catch (Exception ignored) {
                potionTime = 100;
                potionMaxTime = 1000;
            }
            lifeTime = potionTime * 60 + potionMaxTime;
            v9 = potionData;
            if (v9 == null) {
                Intrinsics.throwNpe();
            }
            if (v9.getMaxTimer() == 0 || (double)lifeTime > (double)potionData.getMaxTimer()) {
                potionData.maxTimer = lifeTime;
            }
            state = 0.0f;
            if ((double)lifeTime >= 0.0) {
                state = (float)((double)lifeTime / (double)potionData.getMaxTimer() * 100.0);
            }
            position = Math.round(potionData.translate.getY() + (float)5);
            state = Math.max(state, 2.0f);
            potionData.translate.interpolate(0.0f, y, 0.1);
            potionData.animationX = (float)RenderUtils.getAnimationState2(potionData.getAnimationX(), 1.2f * state, Math.max(10.0f, Math.abs(potionData.animationX - 1.2f * state) * 15.0f) * 0.3f);
            RenderUtils.drawRectPotion(0.0f, potionData.translate.getY(), x2, potionData.translate.getY() + 30.0f, ClientUtils.reAlpha(new Color(34, 24, 20).brighter().getRGB(), 0.3f));
            posY = potionData.translate.getY() + 13.0f;
            Fonts.fontSFUI35.drawString(name + " " + this.intToRomanByGreedy(potionEffect.getAmplifier() + 1), 29.0f, posY - (float)MinecraftInstance.mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha(Colors.WHITE.c, 0.8f));
            Fonts.fontSFUI35.drawString(potionEffect.getDurationString(), 29.0f, posY + 4.0f, ClientUtils.reAlpha(new Color(200, 200, 200).getRGB(), 0.5f));
            if (potion.getHasStatusIcon()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                statusIconIndex = potion.getStatusIconIndex();
                MinecraftInstance.mc.getTextureManager().bindTexture(MinecraftInstance.classProvider.createResourceLocation("textures/gui/container/inventory.png"));
                MinecraftInstance.mc2.field_71456_v.func_175174_a(6.0f, (float)(position + 40), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)2929);
                GlStateManager.func_179121_F();
            }
            y -= 35;
        }
        GlStateManager.func_179121_F();
        return new Border(0.0f, 0.0f, 120.0f, 30.0f);
    }

    private final String intToRomanByGreedy(int num) {
        int num2 = num;
        int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.length && num2 >= 0; ++i) {
            while (values[i] <= num2) {
                num2 -= values[i];
                stringBuilder.append(symbols[i]);
            }
        }
        return stringBuilder.toString();
    }

    public NovolineEffects() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }
}

