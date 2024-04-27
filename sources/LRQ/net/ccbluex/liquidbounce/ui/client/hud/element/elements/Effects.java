/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jx.utils.render.DrawArc;
import jx.utils.render.HanaBiColors;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Effects")
public class Effects
extends Element {
    private final Map<Integer, Integer> potionMaxDurations = new HashMap<Integer, Integer>();
    Map<IPotion, Double> timerMap = new HashMap<IPotion, Double>();
    int animationY = 0;
    int animationX = 0;
    float rectX = 0.0f;
    float giveX = 0.0f;

    @Override
    public Border drawElement() {
        GlStateManager.func_179094_E();
        int tempY = mc.getThePlayer().getActivePotionEffects().size() * 18;
        if (mc.getThePlayer().getActivePotionEffects().size() == 0) {
            this.rectX = 0.0f;
        }
        ArrayList<Float> list = new ArrayList<Float>();
        int y = 2;
        this.animationY = (int)RenderUtils.getAnimationState2(this.animationY, 1.2f * (float)tempY, 350.0);
        this.animationX = (int)RenderUtils.getAnimationState2(this.animationX, 1.2f * this.rectX, 350.0);
        RenderUtils.drawRoundedRect(-5.0f, -5.0f, (float)this.animationX + 2.0f + (float)Fonts.font35.getStringWidth("Effects"), (float)(this.animationY + Fonts.font35.getFontHeight()), 2, new Color(32, 30, 30).getRGB());
        Fonts.font35.drawString("Effects", 0.0f, 0.0f, Color.WHITE.getRGB());
        ArrayList<Integer> needRemove = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> entry : this.potionMaxDurations.entrySet()) {
            if (mc.getThePlayer().getActivePotionEffect(functions.getPotionById(entry.getKey())) != null) continue;
            needRemove.add(entry.getKey());
        }
        Iterator<Object> iterator = needRemove.iterator();
        while (iterator.hasNext()) {
            int id = (Integer)((Object)iterator.next());
            this.potionMaxDurations.remove(id);
        }
        for (IPotionEffect effect : mc.getThePlayer().getActivePotionEffects()) {
            int seconds;
            int minutes;
            if (!this.potionMaxDurations.containsKey(effect.getPotionID()) || this.potionMaxDurations.get(effect.getPotionID()) < effect.getDuration()) {
                this.potionMaxDurations.put(effect.getPotionID(), effect.getDuration());
            }
            IPotion potion = functions.getPotionById(effect.getPotionID());
            String PType = functions.formatI18n(potion.getName(), Arrays.toString(new Object[0]));
            this.giveX = (float)Fonts.font35.getStringWidth(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1) + effect.getDurationString()) + 8.0f;
            list.add(Float.valueOf(this.giveX));
            this.rectX = ((Float)Collections.max(list)).floatValue();
            try {
                minutes = Integer.parseInt(effect.getDurationString().split(":")[0]);
                seconds = Integer.parseInt(effect.getDurationString().split(":")[1]);
            }
            catch (Exception ex) {
                minutes = 0;
                seconds = 0;
            }
            double total = minutes * 60 + seconds;
            if (!this.timerMap.containsKey(potion)) {
                this.timerMap.put(potion, total);
            }
            if (this.timerMap.get(potion) == 0.0 || total > this.timerMap.get(potion)) {
                this.timerMap.replace(potion, total);
            }
            float posY = (float)y + 20.0f;
            Fonts.font35.drawString(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1), 18.0f, posY - (float)mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha(HanaBiColors.WHITE.c, 0.8f));
            Fonts.font35.drawString(effect.getDurationString(), 20.0f + (float)Fonts.font35.getStringWidth(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1)), posY - (float)mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha(new Color(200, 200, 200).getRGB(), 0.5f));
            DrawArc.drawArc((float)this.animationX + 2.0f + (float)Fonts.font35.getStringWidth("Effects") - 16.0f, posY - (float)mc.getFontRendererObj().getFontHeight() + 2.0f, 6.0, new Color(0, 95, 255).getRGB(), 0, 360.0f * ((float)effect.getDuration() / (1.0f * (float)this.potionMaxDurations.get(effect.getPotionID()).intValue())), 4.6f);
            int position = y + 5;
            if (potion.getHasStatusIcon()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int statusIconIndex = potion.getStatusIconIndex();
                mc.getTextureManager().bindTexture(classProvider.createResourceLocation("textures/gui/container/inventory.png"));
                Effects.mc2.field_71456_v.func_175174_a(-2.0f, (float)position, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)2929);
                GlStateManager.func_179121_F();
            }
            y += 20;
        }
        GlStateManager.func_179121_F();
        return new Border(0.0f, 0.0f, 120.0f, 30.0f);
    }

    private String intToRomanByGreedy(int num) {
        int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.length && num >= 0; ++i) {
            while (values[i] <= num) {
                num -= values[i];
                stringBuilder.append(symbols[i]);
            }
        }
        return stringBuilder.toString();
    }
}

