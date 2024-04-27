/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.render.Colors;
import net.dev.important.utils.render.PotionData;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.Translate;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Effect")
public class Effects
extends Element {
    private final Map<Potion, PotionData> potionMap = new HashMap<Potion, PotionData>();

    protected Border draw() {
        GlStateManager.func_179094_E();
        int y = 0;
        for (PotionEffect potionEffect : Effects.mc.field_71439_g.func_70651_bq()) {
            int potionMaxTime;
            int potionTime;
            PotionData potionData;
            Potion potion = Potion.field_76425_a[potionEffect.func_76456_a()];
            String name = I18n.func_135052_a((String)potion.func_76393_a(), (Object[])new Object[0]);
            if (this.potionMap.containsKey(potion) && this.potionMap.get((Object)potion).level == potionEffect.func_76458_c()) {
                potionData = this.potionMap.get(potion);
            } else {
                potionData = new PotionData(potion, new Translate(0.0f, -40.0f + (float)y), potionEffect.func_76458_c());
                this.potionMap.put(potion, potionData);
            }
            boolean flag = true;
            for (PotionEffect checkEffect : Effects.mc.field_71439_g.func_70651_bq()) {
                if (checkEffect.func_76458_c() != potionData.level) continue;
                flag = false;
                break;
            }
            if (flag) {
                this.potionMap.remove(potion);
            }
            try {
                potionTime = Integer.parseInt(Potion.func_76389_a((PotionEffect)potionEffect).split(":")[0]);
                potionMaxTime = Integer.parseInt(Potion.func_76389_a((PotionEffect)potionEffect).split(":")[1]);
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
            potionData.animationX = (float)RenderUtils.getAnimationState(potionData.getAnimationX(), 1.2f * state, Math.max(10.0f, Math.abs(potionData.animationX - 1.2f * state) * 15.0f) * 0.3f);
            RenderUtils.drawRect(0.0f, potionData.translate.getY(), 120.0f, potionData.translate.getY() + 30.0f, RenderUtils.reAlpha(Colors.GREY.c, 0.1f));
            RenderUtils.drawRect(0.0f, potionData.translate.getY(), potionData.animationX, potionData.translate.getY() + 30.0f, RenderUtils.reAlpha(new Color(34, 24, 20).brighter().getRGB(), 0.3f));
            RenderUtils.drawShadow(0, Math.round(potionData.translate.getY()), 120, 30);
            float posY = potionData.translate.getY() + 13.0f;
            Fonts.fontSFUI35.drawString(name + " " + this.intToRomanByGreedy(potionEffect.func_76458_c() + 1), 29.0f, posY - (float)Effects.mc.field_71466_p.field_78288_b, RenderUtils.reAlpha(Colors.WHITE.c, 0.8f));
            Fonts.font35.drawString(Potion.func_76389_a((PotionEffect)potionEffect), 29.0f, posY + 4.0f, RenderUtils.reAlpha(new Color(200, 200, 200).getRGB(), 0.5f));
            if (potion.func_76400_d()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int statusIconIndex = potion.func_76392_e();
                mc.func_110434_K().func_110577_a(new ResourceLocation("textures/gui/container/inventory.png"));
                Effects.mc.field_71456_v.func_175174_a(6.0f, (float)(position + 1), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
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
        int[] values2 = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values2.length && num >= 0; ++i) {
            while (values2[i] <= num) {
                num -= values2[i];
                stringBuilder.append(symbols[i]);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public Border drawElement() {
        return this.draw();
    }
}

