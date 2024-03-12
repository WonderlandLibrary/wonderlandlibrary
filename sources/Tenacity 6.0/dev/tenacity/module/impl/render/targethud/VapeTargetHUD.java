// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.render.StencilUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.utils.animations.ContinualAnimation;

public class VapeTargetHUD extends TargetHUD
{
    private final ContinualAnimation healthAnim;
    private final ContinualAnimation absAnim;
    
    public VapeTargetHUD() {
        super("Vape");
        this.healthAnim = new ContinualAnimation();
        this.absAnim = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        this.setWidth(110.0f);
        this.setHeight(40.0f);
        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x + 4.0f, y + 5.5f, 29.0f, 29.0f, 1.0f, Color.BLACK);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            this.renderPlayer2D(x + 3.0f, y + 4.5f, 31.0f, 31.0f, (AbstractClientPlayer)target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        }
        else {
            RoundedUtil.drawRound(x + 4.0f, y + 5.5f, 29.0f, 29.0f, 1.0f, ColorUtil.applyOpacity(new Color(30, 30, 30), alpha));
            FontUtil.tenacityBoldFont32.drawCenteredStringWithShadow("?", x + 18.5f, y + 20.0f - FontUtil.tenacityBoldFont32.getHeight() / 2.0f, ColorUtil.applyOpacity(-1, alpha));
        }
        VapeTargetHUD.tenacityBoldFont14.drawString(target.getName(), x + 36.5f, y + 5.5f, ColorUtil.applyOpacity(-1, alpha));
        final float targetHealth = target.getHealth();
        final float targetMaxHealth = target.getMaxHealth();
        final float targetAbsorptionAmount = target.getAbsorptionAmount();
        final float targetHealthD = targetHealth / Math.max(targetMaxHealth, 1.0f);
        this.healthAnim.animate(targetHealthD, 18);
        final Color color = ColorUtil.blendColors(new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { new Color(250, 50, 56), new Color(236, 129, 44), new Color(5, 134, 105) }, this.healthAnim.getOutput());
        RoundedUtil.drawRound(x + 37.0f, y + 12.6f + 1.2f, 68.0f, 2.0f, 1.0f, ColorUtil.applyOpacity(new Color(43, 42, 43), alpha));
        RoundedUtil.drawRound(x + 37.0f, y + 12.6f + 1.2f, 68.0f * this.healthAnim.getOutput(), 2.0f, 1.0f, ColorUtil.applyOpacity(color, alpha));
        if (targetAbsorptionAmount > 0.0f) {
            final float absLength = 68.0f * Math.min(targetAbsorptionAmount / targetMaxHealth, 1.0f);
            this.absAnim.animate(absLength, 18);
            RoundedUtil.drawRound(x + 37.0f + Math.max(68.0f * this.healthAnim.getOutput() - this.absAnim.getOutput(), 0.0f), y + 12.6f + 1.2f, this.absAnim.getOutput(), 2.0f, 1.0f, ColorUtil.applyOpacity(new Color(16755200), alpha));
        }
        final String hp = (int)Math.ceil(targetHealth + targetAbsorptionAmount) + " hp";
        VapeTargetHUD.tenacityFont14.drawString(hp, x + 105.0f - VapeTargetHUD.tenacityFont14.getStringWidth(hp), y + 5.5f, ColorUtil.applyOpacity(-1, alpha));
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 36.5f, y + 18.5f + 1.2f, 0.0f);
        GL11.glScaled(0.8, 0.8, 0.8);
        if (target instanceof EntityPlayer) {
            final ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>(Arrays.asList(((EntityPlayer)target).inventory.armorInventory));
            if (((EntityPlayer)target).inventory.getCurrentItem() != null) {
                arrayList.add(((EntityPlayer)target).inventory.getCurrentItem());
            }
            if (arrayList.size() >= 1) {
                int n = 0;
                Collections.reverse(arrayList);
                for (final ItemStack item : arrayList) {
                    RenderHelper.enableGUIStandardItemLighting();
                    VapeTargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(item, n, 0);
                    RenderHelper.disableStandardItemLighting();
                    n += 17;
                }
            }
        }
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        RoundedUtil.drawRound(x + 4.0f, y + 5.5f, 29.0f, 29.0f, 1.0f, ColorUtil.applyOpacity(Color.BLACK, alpha));
        RoundedUtil.drawRound(x + 37.0f, y + 12.6f + 1.2f, 68.0f, 2.0f, 1.0f, ColorUtil.applyOpacity(Color.BLACK, alpha));
    }
}
