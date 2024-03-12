// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import dev.tenacity.scripting.api.objects.ScriptShaderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import jdk.nashorn.api.scripting.JSObject;
import dev.tenacity.utils.render.GradientUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.scripting.api.objects.ScriptFramebuffer;
import dev.tenacity.utils.render.RenderUtil;
import java.awt.Color;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.font.AbstractFontRenderer;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.Utils;

@Exclude({ Strategy.NAME_REMAPPING })
public class RenderBinding implements Utils
{
    private AbstractFontRenderer getFont() {
        return HUDMod.customFont.isEnabled() ? RenderBinding.tenacityFont20 : RenderBinding.mc.fontRendererObj;
    }
    
    public void drawBoundingBox(final EntityLivingBase entityLivingBase, final Color color) {
        RenderUtil.renderBoundingBox(entityLivingBase, color, color.getAlpha() / 255.0f);
    }
    
    public ScriptFramebuffer createFramebuffer() {
        return new ScriptFramebuffer();
    }
    
    public ContinualAnimation newContinualAnimation() {
        return new ContinualAnimation();
    }
    
    public Color applyOpacity(final Color color, final float alpha) {
        return ColorUtil.applyOpacity(color, alpha);
    }
    
    public Color interpolateColors(final Color color1, final Color color2, final float percent) {
        return ColorUtil.interpolateColorC(color1, color2, percent);
    }
    
    public void drawGradientRect(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        GradientUtil.drawGradient(x, y, width, height, alpha, bottomLeft, topLeft, bottomRight, topRight);
    }
    
    public void drawGradientRectHorizontal(final float x, final float y, final float width, final float height, final float alpha, final Color left, final Color right) {
        GradientUtil.drawGradientLR(x, y, width, height, alpha, left, right);
    }
    
    public void drawGradientRectVertical(final float x, final float y, final float width, final float height, final float alpha, final Color top, final Color bottom) {
        GradientUtil.drawGradientTB(x, y, width, height, alpha, top, bottom);
    }
    
    public void applyGradient(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight, final JSObject callback) {
        GradientUtil.applyGradient(x, y, width, height, alpha, bottomLeft, topLeft, bottomRight, topRight, () -> callback.call(null, new Object[0]));
    }
    
    public void drawGradientRoundedRect(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        RoundedUtil.drawGradientRound(x, y, width, height, radius, bottomLeft, topLeft, bottomRight, topRight);
    }
    
    public void drawGradientRoundHorizontal(final float x, final float y, final float width, final float height, final float radius, final Color left, final Color right) {
        RoundedUtil.drawGradientHorizontal(x, y, width, height, radius, left, right);
    }
    
    public void drawGradientRoundVertical(final float x, final float y, final float width, final float height, final float radius, final Color top, final Color bottom) {
        RoundedUtil.drawGradientVertical(x, y, width, height, radius, top, bottom);
    }
    
    public void drawRoundedRect(final float x, final float y, final float width, final float height, final float radius, final Color color) {
        RoundedUtil.drawRound(x, y, width, height, radius, color);
    }
    
    public void drawRect(final float x, final float y, final float width, final float height, final Color color) {
        Gui.drawRect2(x, y, width, height, color.getRGB());
    }
    
    public void renderItem(final int itemSlot, final int x, final int y) {
        final ItemStack stack = RenderBinding.mc.thePlayer.inventoryContainer.getSlot(itemSlot).getStack();
        RenderHelper.enableGUIStandardItemLighting();
        RenderBinding.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        RenderHelper.disableStandardItemLighting();
    }
    
    public float getPartialTicks() {
        return RenderBinding.mc.timer.renderPartialTicks;
    }
    
    public void scissorStart(final double x, final double y, final double width, final double height) {
        RenderUtil.scissorStart(x, y, width, height);
    }
    
    public void scissorEnd() {
        RenderUtil.scissorEnd();
    }
    
    public void drawEntity3D(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        GuiInventory.drawEntityOnScreen(posX, posY, scale, mouseX, mouseY, ent);
    }
    
    public void drawEntity2D(final float x, final float y, final float width, final float height, final EntityLivingBase player) {
        if (player instanceof AbstractClientPlayer) {
            RenderBinding.mc.getTextureManager().bindTexture(((AbstractClientPlayer)player).getLocationSkin());
            GL11.glEnable(3042);
            RenderUtil.resetColor();
            Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8.0f, 8.0f, width, height, 64.0f, 64.0f);
            GL11.glDisable(3042);
        }
    }
    
    public float getScaledWidth() {
        return (float)new ScaledResolution(RenderBinding.mc).getScaledWidth();
    }
    
    public float getScaledHeight() {
        return (float)new ScaledResolution(RenderBinding.mc).getScaledHeight();
    }
    
    public ScriptShaderUtil createShaderUtil(final String fragSource) {
        return new ScriptShaderUtil(fragSource);
    }
    
    public void bindMinecraftFramebuffer() {
        RenderBinding.mc.getFramebuffer().bindFramebuffer(false);
    }
}
