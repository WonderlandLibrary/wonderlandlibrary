// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.utils.misc.MathUtils;
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
import dev.tenacity.utils.animations.ContinualAnimation;

public class OldTenacityTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    
    public OldTenacityTargetHUD() {
        super("Old Tenacity");
        this.animation = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        this.setWidth(Math.max(145.0f, FontUtil.tenacityBoldFont26.getStringWidth(target.getName()) + 40.0f));
        this.setHeight(37.0f);
        final Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        final Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        final Color color = new Color(20, 18, 18, (int)(90.0f * alpha));
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 4.0f, color);
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.renderRoundedRect(x + 3.0f, y + 3.0f, 31.0f, 31.0f, 4.0f, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            this.renderPlayer2D(x + 3.0f, y + 3.0f, 31.0f, 31.0f, (AbstractClientPlayer)target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        }
        else {
            FontUtil.tenacityBoldFont32.drawCenteredStringWithShadow("?", x + 20.0f, y + 17.0f - FontUtil.tenacityBoldFont32.getHeight() / 2.0f, textColor);
        }
        FontUtil.tenacityBoldFont26.drawStringWithShadow(target.getName(), x + 39.0f, y + 5.0f, textColor);
        final float healthPercent = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        final float realHealthWidth = this.getWidth() - 44.0f;
        final float realHealthHeight = 3.0f;
        this.animation.animate(realHealthWidth * healthPercent, 18);
        final Color backgroundHealthColor = new Color(0, 0, 0, (int)alpha * 110);
        final float healthWidth = this.animation.getOutput();
        RoundedUtil.drawRound(x + 39.0f, y + this.getHeight() - 12.0f, 98.0f, realHealthHeight, 1.5f, backgroundHealthColor);
        RoundedUtil.drawGradientHorizontal(x + 39.0f, y + this.getHeight() - 12.0f, healthWidth, realHealthHeight, 1.5f, c1, c2);
        final String healthText = (int)MathUtils.round(healthPercent * 100.0f, 0.01) + "%";
        FontUtil.tenacityFont16.drawStringWithShadow(healthText, x + 34.0f + Math.min(Math.max(1.0f, healthWidth), realHealthWidth - 11.0f), y + this.getHeight() - (14 + FontUtil.tenacityFont16.getHeight()), textColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 4.0f, ColorUtil.applyOpacity(Color.BLACK, alpha));
    }
}
