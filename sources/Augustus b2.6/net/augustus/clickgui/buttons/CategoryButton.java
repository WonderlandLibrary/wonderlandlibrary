// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.buttons;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.GuiButton;

public class CategoryButton extends GuiButton implements MC
{
    private final Color color;
    private Categorys category;
    private double[] cm;
    private boolean unfolded;
    
    public CategoryButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color, final Categorys category) {
        super(id, x, y, width, height, message);
        this.cm = new double[4];
        this.unfolded = true;
        this.color = color;
        this.category = category;
    }
    
    private int getHoverColor(final Color color, final double addBrightness) {
        int colorRGB;
        if (this.hovered) {
            final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            float f = (float)(hsbColor[2] + addBrightness);
            if (hsbColor[2] + addBrightness > 1.0) {
                f = 1.0f;
            }
            else if (hsbColor[2] + addBrightness < 0.0) {
                f = 0.0f;
            }
            colorRGB = Color.HSBtoRGB(hsbColor[0], hsbColor[1], f);
        }
        else {
            colorRGB = color.getRGB();
        }
        return colorRGB;
    }
    
    public void click(final double mouseX, final double mouseY, final int button) {
        if (this.visible && this.enabled) {
            if (this.isMouseOver(mouseX, mouseY)) {
                this.cm = new double[] { mouseX, mouseY, this.xPosition, this.yPosition };
            }
            if (button == 1) {
                final boolean bl = this.mousePressed(CategoryButton.mc, (int)mouseX, (int)mouseY);
                if (bl) {
                    this.playPressSound(CategoryButton.mc.getSoundHandler());
                    this.toggleUnfolded();
                }
            }
        }
    }
    
    public void toggleUnfolded() {
        this.unfolded = !this.unfolded;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
        final FontRenderer fontrenderer = mc.fontRendererObj;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, new Color(41, 146, 222).getRGB());
        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.getHoverColor(this.color, -0.2));
    }
    
    public double[] getCm() {
        return this.cm;
    }
    
    public void setCm(final double[] cmx) {
        this.cm = cmx;
    }
    
    public Categorys getCategory() {
        return this.category;
    }
    
    public void setCategory(final Categorys category) {
        this.category = category;
    }
    
    public boolean isUnfolded() {
        return this.unfolded;
    }
    
    public void setUnfolded(final boolean unfolded) {
        this.unfolded = unfolded;
    }
    
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        return this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}
