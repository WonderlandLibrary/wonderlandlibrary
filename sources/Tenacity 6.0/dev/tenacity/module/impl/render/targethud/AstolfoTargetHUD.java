// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.client.gui.IFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.module.impl.render.HUDMod;
import java.awt.Color;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import java.text.DecimalFormat;
import dev.tenacity.utils.animations.ContinualAnimation;

public class AstolfoTargetHUD extends TargetHUD
{
    private final ContinualAnimation animation;
    private final DecimalFormat DF_1O;
    
    public AstolfoTargetHUD() {
        super("Astolfo");
        this.animation = new ContinualAnimation();
        this.DF_1O = new DecimalFormat("0.#");
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final IFontRenderer fr = AstolfoTargetHUD.mc.fontRendererObj;
        final float width = Math.max(110.0f, fr.getStringWidth(target.getName()) / 1.5f + 100.0f);
        final double healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0.0f, 1.0f);
        final float healthWidth = this.animation.getOutput();
        this.setWidth(width);
        this.setHeight(45.0f);
        final Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        final Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        Gui.drawRect2(x, y, width, 50.0, new Color(0.0f, 0.0f, 0.0f, 0.6f * alpha).getRGB());
        RenderUtil.drawGradientRect(x + 34.0f, y + 40.0f, x + width + 6.0f, y + 48.0f, c1.darker().darker().darker().darker().getRGB(), c2.darker().darker().darker().darker().getRGB());
        final float endWidth = (float)Math.max(0.0, (width - 34.0f) * healthPercentage);
        this.animation.animate(endWidth, 18);
        RenderUtil.drawGradientRect(x + 34.0f, y + 40.0f, x + 50.0f + healthWidth / 2.0f, y + 48.0f, c1.darker().darker().getRGB(), c2.darker().darker().getRGB());
        RenderUtil.drawGradientRect(x + 34.0f, y + 40.0f, x + 50.0f + Math.min(endWidth, healthWidth) / 2.0f, y + 48.0f, c1.getRGB(), c2.getRGB());
        RenderUtil.resetColor();
        RenderUtil.color(-1, alpha);
        GuiInventory.drawEntityOnScreen((int)x + 17, (int)y + 47, 23, target.rotationYaw, target.rotationPitch, target);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        fr.drawStringWithShadow(target.getName(), x + 32.5f, y + 5.0f, ColorUtil.applyOpacity(-1, (float)Math.max(0.1, alpha)));
        final float scale = 2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        fr.drawStringWithShadow(this.DF_1O.format(target.getHealth()) + " \u2764", (x + 32.5f) / scale, (y + 16.0f) / scale, ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), (float)Math.max(0.1, alpha)));
        GlStateManager.popMatrix();
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        Gui.drawRect2(x, y, this.getWidth(), 45.0, ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }
}
