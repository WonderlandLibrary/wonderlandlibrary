// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.ui.widgets;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.client.gui.GuiButton;

public class CustomButton extends GuiButton
{
    private final Color color;
    private boolean lastHovered;
    private int anim;
    private int anim2;
    
    public CustomButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color) {
        super(id, x, y, width, height, message);
        this.lastHovered = false;
        this.color = color;
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
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(CustomButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (!this.lastHovered && this.hovered) {
                this.lastHovered = true;
                this.anim = 0;
                this.anim2 = 20;
            }
            if (this.lastHovered && !this.hovered) {
                this.lastHovered = false;
            }
            if (this.hovered) {
                if (this.anim2 >= 5) {
                    this.anim2 -= (int)0.1;
                }
                this.anim += this.anim2;
                if (this.anim > this.width) {
                    this.anim = this.width;
                }
            }
            else {
                if (this.anim2 >= 5) {
                    this.anim2 -= (int)0.1;
                }
                this.anim -= this.anim2;
                if (this.anim < 0) {
                    this.anim = 0;
                }
            }
            Gui.drawRect(this.xPosition, this.yPosition, this.anim + this.xPosition, (int)(float)(this.height + this.yPosition), this.getHoverColor(this.color, -0.2));
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (!this.enabled) {
                j = 10526880;
            }
            else if (this.hovered) {
                j = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }
}
