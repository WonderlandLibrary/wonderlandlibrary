// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.utils.font.CustomFont;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import java.text.DecimalFormat;
import dev.tenacity.utils.animations.ContinualAnimation;

public class AkrienTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    private final DecimalFormat DF_1;
    
    public AkrienTargetHUD() {
        super("Akrien");
        this.animation = new ContinualAnimation();
        this.DF_1 = new DecimalFormat("0.0");
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final CustomFont rubikBold = AkrienTargetHUD.rubikFont.boldSize(18);
        final CustomFont rubikSmall = AkrienTargetHUD.rubikFont.size(13);
        this.setWidth(Math.max(100.0f, rubikBold.getStringWidth(target.getName()) + 45.0f));
        this.setHeight(39.5f);
        final double healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        final int bg = new Color(0.0f, 0.0f, 0.0f, 0.4f * alpha).getRGB();
        Gui.drawRect(x, y, x + this.getWidth(), y + 39.5f, bg);
        Gui.drawRect2(x + 2.5, y + 31.0f, this.getWidth() - 4.5, 2.5, bg);
        Gui.drawRect2(x + 2.5, y + 34.5, this.getWidth() - 4.5, 2.5, bg);
        final float endWidth = (float)Math.max(0.0, (this.getWidth() - 3.5) * healthPercentage);
        this.animation.animate(endWidth, 18);
        if (this.animation.getOutput() > 0.0f) {
            RenderUtil.drawGradientRectBordered(x + 2.5, y + 31.0f, x + 1.5 + this.animation.getOutput(), y + 33.5, 0.74, ColorUtil.applyOpacity(-16737215, alpha), ColorUtil.applyOpacity(-7405631, alpha), bg, bg);
        }
        final double armorValue = target.getTotalArmorValue() / 20.0;
        if (armorValue > 0.0) {
            RenderUtil.drawGradientRectBordered(x + 2.5, y + 34.5, x + 1.5 + (this.getWidth() - 3.5) * armorValue, y + 37.0f, 0.74, ColorUtil.applyOpacity(-16750672, alpha), ColorUtil.applyOpacity(-12986881, alpha), bg, bg);
        }
        GlStateManager.pushMatrix();
        RenderUtil.setAlphaLimit(0.0f);
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        if (target instanceof AbstractClientPlayer) {
            RenderUtil.color(textColor);
            final float f = 0.8125f;
            GlStateManager.scale(f, f, f);
            this.renderPlayer2D(x / f + 3.0f, y / f + 3.0f, 32.0f, 32.0f, (AbstractClientPlayer)target);
        }
        else {
            Gui.drawRect2(x + 3.0f, y + 3.0f, 25.0, 25.0, bg);
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            rubikBold.drawStringWithShadow("?", (x + 11.0f) / 2.0f, (y + 11.0f) / 2.0f, textColor);
        }
        GlStateManager.popMatrix();
        rubikBold.drawString(target.getName(), x + 31.0f, y + 5.0f, textColor);
        rubikSmall.drawString("Health: " + this.DF_1.format(target.getHealth()), x + 31.0f, y + 15.0f, textColor);
        rubikSmall.drawString("Distance: " + this.DF_1.format(AkrienTargetHUD.mc.thePlayer.getDistanceToEntity(target)) + "m", x + 31.0f, y + 22.0f, textColor);
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        Gui.drawRect(x, y, x + this.getWidth(), y + 39.5f, ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }
}
