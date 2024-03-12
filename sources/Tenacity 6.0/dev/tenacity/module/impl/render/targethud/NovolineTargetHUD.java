// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.client.gui.IFontRenderer;
import dev.tenacity.utils.misc.MathUtils;
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

public class NovolineTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    
    public NovolineTargetHUD() {
        super("Novoline");
        this.animation = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final IFontRenderer fr = NovolineTargetHUD.mc.fontRendererObj;
        final double healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        final Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        final Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        this.setWidth(Math.max(120.0f, fr.getStringWidth(target.getName()) + 50.0f));
        this.setHeight(34.0f);
        final int alphaInt = (int)(alpha * 255.0f);
        Gui.drawRect2(x, y, this.getWidth(), this.getHeight(), new Color(29, 29, 29, alphaInt).getRGB());
        Gui.drawRect2(x + 1.0f, y + 1.0f, this.getWidth() - 2.0f, this.getHeight() - 2.0f, new Color(40, 40, 40, alphaInt).getRGB());
        Gui.drawRect2(x + 34.0f, y + 15.0f, 83.0, 10.0, ColorUtil.applyOpacity(-14213603, alpha));
        final float f = (float)(83.0 * healthPercentage);
        this.animation.animate(f, 40);
        RenderUtil.drawGradientRect(x + 34.0f, y + 15.0f, x + 34.0f + this.animation.getOutput(), y + 25.0f, c1.darker().darker().getRGB(), c2.darker().darker().getRGB());
        RenderUtil.drawGradientRect(x + 34.0f, y + 15.0f, x + 34.0f + f, y + 25.0f, c1.getRGB(), c2.getRGB());
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        final int mcTextColor = ColorUtil.applyOpacity(-1, (float)Math.max(0.1, alpha));
        GLUtil.startBlend();
        if (target instanceof AbstractClientPlayer) {
            RenderUtil.color(textColor);
            this.renderPlayer2D(x + 3.5f, y + 3.0f, 28.0f, 28.0f, (AbstractClientPlayer)target);
        }
        else {
            fr.drawStringWithShadow("?", x + 17.0f - fr.getStringWidth("?") / 2.0f, y + 17.0f - fr.FONT_HEIGHT / 2.0f, mcTextColor);
        }
        GLUtil.startBlend();
        fr.drawStringWithShadow(target.getName(), x + 40.0f, y + 4.0f, mcTextColor);
        final String healthText = MathUtils.round(healthPercentage * 100.0, 0.01) + "%";
        fr.drawStringWithShadow(healthText, x + 17.0f + this.getWidth() / 2.0f - fr.getStringWidth(healthText) / 2.0f, y + 16.0f, mcTextColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        Gui.drawRect2(x, y, this.getWidth(), this.getHeight(), ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }
}
