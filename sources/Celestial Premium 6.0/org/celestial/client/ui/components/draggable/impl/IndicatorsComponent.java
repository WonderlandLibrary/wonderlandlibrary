/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 */
package org.celestial.client.ui.components.draggable.impl;

import java.awt.Color;
import javafx.animation.Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.Indicators;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class IndicatorsComponent
extends DraggableModule {
    private float cooldownAnim;
    private float hurtAnim;

    public IndicatorsComponent() {
        super("IndicatorsComponent", 40, 220);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void draw() {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(Indicators.class).getState()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 48.0f + 70.0f;
        float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (Indicators.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), 142.0, 60.0, (int)Indicators.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(left, top, right, bottom, new Color(5, 5, 5, 175).getRGB());
        RectHelper.drawSmoothRect(x, y, right, y - 1.0f, ClientHelper.getClientColor().getRGB());
        if (Indicators.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor(), (double)((int)x), (double)((int)y), 120.0, 3.0, 20);
        }
        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
        this.mc.comfortaa.drawStringWithShadow("Indicators", x + 33.0f, y + 5.0f, -1);
        if (Minecraft.getDebugFPS() > 5) {
            RectHelper.drawRectBetter(left + 49.0f, top + 23.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(left + 49.0f, top + 34.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
            if (String.valueOf(this.cooldownAnim).equals("NaN") || String.valueOf(this.hurtAnim).equals("NaN")) {
                this.hurtAnim = this.mc.player.hurtTime;
                this.cooldownAnim = this.mc.player.getCooledAttackStrength(0.0f);
            }
            float value = Minecraft.getDebugFPS() < 10 ? 0.25f : 6.0f / (float)Minecraft.getDebugFPS();
            this.cooldownAnim = (float)Interpolator.LINEAR.interpolate((double)this.cooldownAnim, (double)this.mc.player.getCooledAttackStrength(-1.0f), (double)value);
            this.cooldownAnim = MathHelper.clamp(this.cooldownAnim, 0.01f, 1.0f);
            double endValue = MathHelper.clamp((double)((float)this.mc.player.hurtTime / 7.0f), 0.1, 1.0);
            this.hurtAnim = (float)Interpolator.LINEAR.interpolate((double)this.hurtAnim, (double)((float)endValue), (double)(6.0f / (float)Minecraft.getDebugFPS()));
            if (Indicators.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)(x + 49.0f), (double)(y + 23.0f), (double)(this.cooldownAnim * 60.0f), 2.0, 10);
                RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)(x + 49.0f), (double)(y + 34.0f), (double)(this.hurtAnim * 60.0f), 2.0, 10);
            }
        }
        this.mc.fontRenderer.drawStringWithShadow("Cooldown", left + 3.0f, top + 20.0f, -1);
        RectHelper.drawSmoothRectBetter(left + 49.0f, top + 23.0f, this.cooldownAnim * 60.0f, 2.0, ClientHelper.getClientColor().getRGB());
        this.mc.fontRenderer.drawStringWithShadow("HurtTime", left + 3.0f, top + 31.0f, -1);
        RectHelper.drawSmoothRectBetter(left + 49.0f, top + 34.0f, this.hurtAnim * 60.0f, 2.0, ClientHelper.getClientColor().getRGB());
        GlStateManager.popMatrix();
        super.draw();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(Indicators.class).getState()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 48.0f + 70.0f;
        float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (Indicators.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), 142.0, 60.0, (int)Indicators.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(left, top, right, bottom, new Color(5, 5, 5, 175).getRGB());
        RectHelper.drawSmoothRect(x, y, right, y - 1.0f, ClientHelper.getClientColor().getRGB());
        if (Indicators.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor(), (double)((int)x), (double)((int)y), 120.0, 3.0, 20);
        }
        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
        this.mc.comfortaa.drawStringWithShadow("Indicators", x + 33.0f, y + 5.0f, -1);
        if (Minecraft.getDebugFPS() > 5) {
            RectHelper.drawRectBetter(left + 49.0f, top + 23.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(left + 49.0f, top + 34.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
            if (String.valueOf(this.cooldownAnim).equals("NaN") || String.valueOf(this.hurtAnim).equals("NaN")) {
                this.hurtAnim = this.mc.player.hurtTime;
                this.cooldownAnim = this.mc.player.getCooledAttackStrength(0.0f);
            }
            float value = Minecraft.getDebugFPS() < 10 ? 0.25f : 6.0f / (float)Minecraft.getDebugFPS();
            this.cooldownAnim = (float)Interpolator.LINEAR.interpolate((double)this.cooldownAnim, (double)this.mc.player.getCooledAttackStrength(-1.0f), (double)value);
            this.cooldownAnim = MathHelper.clamp(this.cooldownAnim, 0.01f, 1.0f);
            double endValue = MathHelper.clamp((double)((float)this.mc.player.hurtTime / 7.0f), 0.1, 1.0);
            this.hurtAnim = (float)Interpolator.LINEAR.interpolate((double)this.hurtAnim, (double)((float)endValue), (double)(6.0f / (float)Minecraft.getDebugFPS()));
            if (Indicators.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)(x + 49.0f), (double)(y + 23.0f), (double)(this.cooldownAnim * 60.0f), 2.0, 10);
                RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)(x + 49.0f), (double)(y + 34.0f), (double)(this.hurtAnim * 60.0f), 2.0, 10);
            }
        }
        this.mc.fontRenderer.drawStringWithShadow("Cooldown", left + 3.0f, top + 20.0f, -1);
        RectHelper.drawSmoothRectBetter(left + 49.0f, top + 23.0f, this.cooldownAnim * 60.0f, 2.0, ClientHelper.getClientColor().getRGB());
        this.mc.fontRenderer.drawStringWithShadow("HurtTime", left + 3.0f, top + 31.0f, -1);
        RectHelper.drawSmoothRectBetter(left + 49.0f, top + 34.0f, this.hurtAnim * 60.0f, 2.0, ClientHelper.getClientColor().getRGB());
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }
}

