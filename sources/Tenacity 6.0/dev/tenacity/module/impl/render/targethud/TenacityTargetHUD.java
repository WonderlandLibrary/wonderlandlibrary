// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.entity.Entity;
import dev.tenacity.utils.misc.MathUtils;
import java.awt.Color;
import dev.tenacity.utils.render.StencilUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.utils.animations.ContinualAnimation;

public class TenacityTargetHUD extends TargetHUD
{
    private final ContinualAnimation animatedHealthBar;
    
    public TenacityTargetHUD() {
        super("Tenacity");
        this.animatedHealthBar = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        this.setWidth(Math.max(155.0f, TenacityTargetHUD.tenacityBoldFont22.getStringWidth(target.getName()) + 75.0f));
        this.setHeight(50.0f);
        RenderUtil.setAlphaLimit(0.0f);
        final float colorAlpha = 0.8f * alpha;
        RoundedUtil.drawGradientRound(x, y, this.getWidth(), this.getHeight(), 6.0f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), colorAlpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), colorAlpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), colorAlpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), colorAlpha));
        final float size = 38.0f;
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.circleNoSmoothRGB(x + 10.0f, y + this.getHeight() / 2.0f - size / 2.0f, size, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.resetColor();
            RenderUtil.setAlphaLimit(0.0f);
            RenderUtil.color(textColor);
            this.renderPlayer2D(x + 10.0f, y + this.getHeight() / 2.0f - size / 2.0f, size, size, (AbstractClientPlayer)target);
            StencilUtil.uninitStencilBuffer();
        }
        else {
            TenacityTargetHUD.tenacityBoldFont32.drawCenteredStringWithShadow("?", x + 30.0f, y + 25.0f - TenacityTargetHUD.tenacityBoldFont32.getHeight() / 2.0f, textColor);
        }
        TenacityTargetHUD.tenacityBoldFont22.drawCenteredString(target.getName(), x + 10.0f + size + (this.getWidth() - (10.0f + size)) / 2.0f, y + 10.0f, textColor);
        final float healthPercentage = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
        final float healthBarWidth = this.getWidth() - (size + 30.0f);
        final float newHealthWidth = healthBarWidth * healthPercentage;
        this.animatedHealthBar.animate(newHealthWidth, 18);
        RoundedUtil.drawRound(x + 20.0f + size, y + 25.0f, healthBarWidth, 4.0f, 2.0f, ColorUtil.applyOpacity(Color.BLACK, 0.3f * alpha));
        RoundedUtil.drawRound(x + 20.0f + size, y + 25.0f, this.animatedHealthBar.getOutput(), 4.0f, 2.0f, ColorUtil.applyOpacity(Color.WHITE, alpha));
        final String healthText = MathUtils.DF_0.format(healthPercentage * 100.0f) + "%";
        TenacityTargetHUD.tenacityFont18.drawCenteredString(healthText + " - " + Math.round(target.getDistanceToEntity(TenacityTargetHUD.mc.thePlayer)) + "m", x + 10.0f + size + (this.getWidth() - (10.0f + size)) / 2.0f, y + 35.0f, textColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        if (glow) {
            RoundedUtil.drawGradientRound(x, y, this.getWidth(), this.getHeight(), 6.0f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), alpha));
        }
        else {
            RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 6.0f, ColorUtil.applyOpacity(Color.BLACK, alpha));
        }
    }
}
