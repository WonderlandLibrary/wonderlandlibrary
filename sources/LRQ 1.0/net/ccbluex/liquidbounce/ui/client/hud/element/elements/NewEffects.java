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
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.HanaBiColors;
import net.ccbluex.liquidbounce.utils.render.PotionData;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Translate;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="NewEffects")
public class NewEffects
extends Element {
    private static final FloatValue radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
    private final Map<IPotion, PotionData> potionMap = new HashMap<IPotion, PotionData>();

    @Override
    public Border drawElement() {
        GlStateManager.func_179094_E();
        int y = 0;
        for (IPotionEffect potionEffect : mc.getThePlayer().getActivePotionEffects()) {
            int potionMaxTime;
            int potionTime;
            PotionData potionData;
            IPotion potion = functions.getPotionById(potionEffect.getPotionID());
            String name = functions.formatI18n(potion.getName(), new String[0]);
            if (this.potionMap.containsKey(potion) && this.potionMap.get((Object)potion).level == potionEffect.getAmplifier()) {
                potionData = this.potionMap.get(potion);
            } else {
                potionData = new PotionData(potion, new Translate(0.0f, -40.0f + (float)y), potionEffect.getAmplifier());
                this.potionMap.put(potion, potionData);
            }
            boolean flag = true;
            for (IPotionEffect checkEffect : mc.getThePlayer().getActivePotionEffects()) {
                if (checkEffect.getAmplifier() != potionData.level) continue;
                flag = false;
                break;
            }
            if (flag) {
                this.potionMap.remove(potion);
            }
            try {
                potionTime = Integer.parseInt(potionEffect.getDurationString().split(":")[0]);
                potionMaxTime = Integer.parseInt(potionEffect.getDurationString().split(":")[1]);
            }
            catch (Exception ignored) {
                potionTime = 100;
                potionMaxTime = 1000;
            }
            int lifeTime = potionTime * 60 + potionMaxTime;
            if (potionData.getMaxTimer() == 0 || (double)lifeTime > (double)potionData.getMaxTimer()) {
                potionData.maxTimer = lifeTime;
            }
            float state = 0.0f;
            if ((double)lifeTime >= 0.0) {
                state = (float)((double)lifeTime / (double)potionData.getMaxTimer() * 100.0);
            }
            int position = Math.round(potionData.translate.getY() + 5.0f);
            state = Math.max(state, 2.0f);
            potionData.translate.interpolate(0.0f, y, 0.1);
            potionData.animationX = (float)RenderUtils.getAnimationState2(potionData.getAnimationX(), 1.2f * state, Math.max(10.0f, Math.abs(potionData.animationX - 1.2f * state) * 15.0f) * 0.3f);
            RenderUtils.drawRectPotion(0.0f, potionData.translate.getY(), 120.0f, potionData.translate.getY() + 30.0f, ClientUtils.reAlpha(HanaBiColors.GREY.c, 0.1f));
            RenderUtils.drawShadowWithCustomAlpha(0.0f, Math.round(potionData.translate.getY()), 120.0f, 30.0f, 200.0f);
            float posY = potionData.translate.getY() + 13.0f;
            Fonts.fontSFUI35.drawString(name + " " + this.intToRomanByGreedy(potionEffect.getAmplifier() + 1), 29.0f, posY - (float)mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha(HanaBiColors.WHITE.c, 0.8f));
            Fonts.font35.drawString(potionEffect.getDurationString(), 29.0f, posY + 4.0f, ClientUtils.reAlpha(new Color(180, 180, 180).getRGB(), 0.5f));
            if (potion.getHasStatusIcon()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int statusIconIndex = potion.getStatusIconIndex();
                mc.getTextureManager().bindTexture(classProvider.createResourceLocation("textures/gui/container/inventory.png"));
                NewEffects.mc2.field_71456_v.func_175174_a(6.0f, (float)(position + 40), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
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

