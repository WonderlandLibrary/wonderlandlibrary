// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.entity.Entity;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import net.minecraft.entity.EntityLivingBase;

public class ExhiTargetHUD extends TargetHUD
{
    public ExhiTargetHUD() {
        super("Exhibition");
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        this.setWidth(Math.max(135.0f, ExhiTargetHUD.tenacityFont20.getStringWidth("Name: bruh") + 60.0f));
        this.setHeight(46.0f);
        final Color darkest = ColorUtil.applyOpacity(new Color(10, 10, 10), alpha);
        final Color secondDarkest = ColorUtil.applyOpacity(new Color(22, 22, 22), alpha);
        final Color lightest = ColorUtil.applyOpacity(new Color(44, 44, 44), alpha);
        final Color middleColor = ColorUtil.applyOpacity(new Color(34, 34, 34), alpha);
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, alpha);
        Gui.drawRect2(x - 3.5, y - 3.5, this.getWidth() + 7.0f, this.getHeight() + 7.0f, darkest.getRGB());
        Gui.drawRect2(x - 3.0f, y - 3.0f, this.getWidth() + 6.0f, this.getHeight() + 6.0f, middleColor.getRGB());
        Gui.drawRect2(x - 1.0f, y - 1.0f, this.getWidth() + 2.0f, this.getHeight() + 2.0f, lightest.getRGB());
        Gui.drawRect2(x, y, this.getWidth(), this.getHeight(), secondDarkest.getRGB());
        final float size = this.getHeight() - 6.0f;
        Gui.drawRect2(x + 3.0f, y + 3.0f, 0.5, size, lightest.getRGB());
        Gui.drawRect2(x + 3.0f, y + 3.0f + size, size, 0.5, lightest.getRGB());
        Gui.drawRect2(x + 3.0f + size, y + 3.0f, 0.5, size + 0.5f, lightest.getRGB());
        Gui.drawRect2(x + 3.0f, y + 3.0f, size, 0.5, lightest.getRGB());
        final int alphaInt = (int)(255.0f * alpha);
        ExhiTargetHUD.tahomaFont.boldSize(16).drawString(target.getName(), x + 8.0f + size, y + 6.0f, textColor.getRGB());
        final float healthValue = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
        Color healthColor = (healthValue > 0.5f) ? ColorUtil.interpolateColorC(new Color(255, 255, 10), new Color(10, 255, 10), (healthValue - 0.5f) / 0.5f) : ColorUtil.interpolateColorC(new Color(255, 10, 10), new Color(255, 255, 10), healthValue * 2.0f);
        healthColor = ColorUtil.applyOpacity(healthColor, alpha);
        final float healthBarWidth = this.getWidth() - (size + 12.0f);
        Gui.drawRect2(x + 8.0f + size, y + 15.0f, healthBarWidth, 5.0, darkest.getRGB());
        Gui.drawRect2(x + 8.0f + size + 0.5, y + 15.5f, healthBarWidth - 1.0f, 4.0, ColorUtil.interpolateColor(darkest, healthColor, 0.2f));
        final float heathBarActualWidth = healthBarWidth - 1.0f;
        Gui.drawRect2(x + 8.0f + size + 0.5, y + 15.5f, heathBarActualWidth * healthValue, 4.0, healthColor.getRGB());
        final float increment = heathBarActualWidth / 11.0f;
        for (int i = 1; i < 11; ++i) {
            Gui.drawRect2(x + 8.0f + size + increment * i, y + 15.5f, 0.5, 4.0, darkest.getRGB());
        }
        ExhiTargetHUD.tahomaFont.size(12).drawString("HP: " + MathUtils.round(target.getHealth() + target.getAbsorptionAmount(), 1) + " | Dist: " + MathUtils.round(ExhiTargetHUD.mc.thePlayer.getDistanceToEntity(target), 1), x + 8.0f + size, y + 25.0f, textColor.getRGB());
        final float seperation = healthBarWidth / 5.0f;
        GLUtil.startBlend();
        RenderUtil.color(textColor.getRGB());
        GuiInventory.drawEntityOnScreen((int)(x + 3.0f + size / 2.0f), (int)(y + size + 1.0f), 18, target.rotationYaw, -target.rotationPitch, target);
        RenderHelper.enableGUIStandardItemLighting();
        for (int j = 0; j <= 3; ++j) {
            if (target.getCurrentArmor(j) != null) {
                RenderUtil.resetColor();
                GLUtil.startBlend();
                RenderUtil.color(textColor.getRGB());
                ExhiTargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(j), (int)(x + size + 7.0f + seperation * (3 - j)), (int)(y + 28.0f));
                RenderUtil.drawExhiEnchants(target.getCurrentArmor(j), (float)(int)(x + size + 7.0f + seperation * (3 - j)), (float)(int)(y + 30.0f));
                GLUtil.endBlend();
            }
        }
        if (target.getHeldItem() != null) {
            GLUtil.startBlend();
            RenderUtil.resetColor();
            RenderUtil.color(textColor.getRGB());
            ExhiTargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItem(), (int)(x + size + 7.0f + seperation * 4.0f), (int)(y + 28.0f));
            RenderUtil.drawExhiEnchants(target.getHeldItem(), (float)(int)(x + size + 7.0f + seperation * 4.0f), (float)(int)(y + 30.0f));
            GLUtil.endBlend();
        }
        RenderHelper.disableStandardItemLighting();
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        Gui.drawRect2(x - 3.5, y - 3.5, this.getWidth() + 7.0f, this.getHeight() + 7.0f, ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }
}
