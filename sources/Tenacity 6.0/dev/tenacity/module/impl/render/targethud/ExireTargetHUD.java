// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.client.entity.AbstractClientPlayer;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.module.impl.render.HUDMod;
import java.awt.Color;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.utils.animations.ContinualAnimation;

public class ExireTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    
    public ExireTargetHUD() {
        super("Exire");
        this.animation = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final float healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        final Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        final Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        this.setWidth(ExireTargetHUD.tenacityBoldFont28.getStringWidth(target.getName()));
        this.setHeight(30.0f);
        final int alphaInt = (int)(alpha * 255.0f);
        final Color black = new Color(0, 0, 0, alphaInt);
        Gui.drawRect2(x + 1.0f, y + 1.0f, this.getWidth() + 30.0f, this.getHeight() - 2.0f, new Color(28, 28, 28, alphaInt).getRGB());
        final float f = 83.0f * healthPercentage;
        this.animation.animate(f, 40);
        RenderUtil.drawGradientRect(x + 29.5f, y + 20.0f, x + 28.5f + this.getWidth(), y + 26.5f, black.getRGB(), black.getRGB());
        RenderUtil.drawGradientRect(x + 29.5f, y + 20.0f, x + (28.5f + this.getWidth()) * healthPercentage, y + 26.5f, c1.getRGB(), c2.getRGB());
        this.animation.animate(this.getWidth() * healthPercentage, 30);
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        final int mcTextColor = ColorUtil.applyOpacity(-1, (float)Math.max(0.1, alpha));
        GLUtil.startBlend();
        if (target instanceof AbstractClientPlayer) {
            RenderUtil.color(textColor);
            this.renderPlayer2D(x + 3.5f, y + 3.0f, 23.5f, 23.5f, (AbstractClientPlayer)target);
        }
        else {
            ExireTargetHUD.fr.drawStringWithShadow("?", x + 17.0f - ExireTargetHUD.fr.getStringWidth("?") / 2.0f, y + 17.0f - ExireTargetHUD.fr.FONT_HEIGHT / 2.0f, mcTextColor);
        }
        GLUtil.startBlend();
        ExireTargetHUD.tenacityBoldFont28.drawStringWithShadow(target.getName(), x + 28.5f, y + 4.0f, mcTextColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        Gui.drawRect2(x, y, this.getWidth(), this.getHeight(), ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }
}
