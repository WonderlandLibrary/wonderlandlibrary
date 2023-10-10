// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.widgets;

import net.minecraft.client.gui.FontRenderer;
import net.augustus.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.augustus.Augustus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.client.gui.GuiButton;

public class ConfigButton extends GuiButton
{
    private final Color color;
    private String date;
    private String time;
    private boolean selected;
    private float mouseDelta;
    
    public ConfigButton(final int id, final int x, final int y, final int width, final int height, final String name, final String date, final String time, final Color color) {
        super(id, x, y, width, height, name);
        this.color = color;
        this.date = date;
        this.time = time;
    }
    
    public void draw(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(ConfigButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            Gui.drawRect(this.xPosition, this.yPosition, (int)(float)(this.width + this.xPosition), (int)(float)(this.height + this.yPosition), this.getHoverColor(Augustus.getInstance().getClientColor(), -0.2));
            this.mouseDragged(mc, mouseX, mouseY);
            int j = Color.gray.getRGB();
            if (!this.selected) {
                j = 14737632;
            }
            fontrenderer.drawStringWithShadow(this.displayString, (float)(this.xPosition + 5), this.yPosition - fontrenderer.FONT_HEIGHT / 2.0f + this.height / 2.0f, j);
            fontrenderer.drawStringWithShadow(this.date, (float)(this.xPosition + this.width - 5 - fontrenderer.getStringWidth(this.date)), this.yPosition - 0.5f - fontrenderer.FONT_HEIGHT + this.height / 2.0f, j);
            fontrenderer.drawStringWithShadow(this.time, (float)(this.xPosition + this.width - 5 - fontrenderer.getStringWidth(this.time)), this.yPosition + 0.5f + this.height / 2.0f, j);
            RenderUtil.drawFloatRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), this.yPosition - 0.5f, Color.black.getRGB());
            RenderUtil.drawFloatRect((float)this.xPosition, this.yPosition + this.height - 0.5f, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), Color.black.getRGB());
        }
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
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(final String date) {
        this.date = date;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public void setTime(final String time) {
        this.time = time;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
