// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.StencilUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.module.impl.render.HUDMod;
import java.awt.Color;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.entity.EntityLivingBase;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import dev.tenacity.utils.animations.ContinualAnimation;

public class AdaptTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    private final DecimalFormatSymbols symbols;
    private final DecimalFormat DF_1;
    
    public AdaptTargetHUD() {
        super("Adapt");
        this.animation = new ContinualAnimation();
        this.symbols = new DecimalFormatSymbols(Locale.US);
        this.DF_1 = new DecimalFormat("0.0#", this.symbols);
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        this.setWidth(Math.max(115.0f, FontUtil.tenacityBoldFont22.getStringWidth(target.getName()) + 40.0f));
        this.setHeight(37.0f);
        final Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        final Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        final Color color = new Color(20, 18, 18, (int)(90.0f * alpha));
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 5.0f, color);
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.renderRoundedRect(x + 3.0f, y + 3.0f, 31.0f, 31.0f, 6.0f, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            this.renderPlayer2D(x + 3.0f, y + 3.0f, 31.0f, 31.0f, (AbstractClientPlayer)target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        }
        else {
            FontUtil.tenacityBoldFont32.drawCenteredStringWithShadow("?", x + 20.0f, y + 17.0f - FontUtil.tenacityBoldFont32.getHeight() / 2.0f, textColor);
        }
        final float realHealthHeight = 5.0f;
        final float realHealthWidth = this.getWidth() - 44.0f;
        final float healthWidth = this.animation.getOutput();
        final float healthPercent = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        RoundedUtil.drawGradientHorizontal(x + 37.5f, y + this.getHeight() - 10.5f, healthWidth - 7.5f, realHealthHeight, 2.5f, c1, c2);
        this.animation.animate(realHealthWidth * healthPercent, 18);
        FontUtil.tenacityBoldFont18.drawStringWithShadow(target.getName(), x + 35.5f, y + 5.0f, textColor);
        final double distance = AdaptTargetHUD.mc.thePlayer.getDistanceToEntity(target);
        FontUtil.tenacityFont18.drawStringWithShadow("Distance: " + this.DF_1.format(distance), x + 35.5f, y + 15.0f, textColor);
        final float targetHealth = target.getHealth();
        final float targetAbsorptionAmount = target.getAbsorptionAmount();
        final String healthText = String.valueOf((int)Math.ceil(targetHealth + targetAbsorptionAmount));
        FontUtil.tenacityBoldFont16.drawStringWithShadow(healthText, x + healthWidth + 32.5f, y + this.getHeight() - 11.8f, textColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 5.0f, ColorUtil.applyOpacity(Color.BLACK, alpha));
    }
}
