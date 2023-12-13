// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.widgets;

import net.minecraft.client.gui.FontRenderer;
import net.augustus.Augustus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.lenni0451.eventapi.manager.EventManager;
import java.awt.Color;
import net.minecraft.client.gui.GuiButton;

public class BGShaderButton extends GuiButton
{
    private final Color color;
    private final Color toggledColor;
    
    public BGShaderButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color, final Color toggledColor) {
        super(id, x, y, width, height, message);
        this.color = color;
        this.toggledColor = toggledColor;
        EventManager.register((Object)this);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final ScaledResolution sr = new ScaledResolution(mc);
            final int windowHeight = sr.getScaledHeight();
            final double scale = sr.getScaleFactor();
            final int scaledWidth = (int)(this.width * scale);
            final int scaledHeight = (int)(this.height * scale);
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(BGShaderButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.mouseDragged(mc, mouseX, mouseY);
            final int y = (int)(this.yPosition + (this.height - 8.0f) / 2.0f);
            if (Augustus.getInstance().getBackgroundShaderUtil().getCurrentShader().getName().equalsIgnoreCase(this.displayString)) {
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, y, this.getHoverColor(this.toggledColor, 0.2));
            }
            else {
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, y, this.getHoverColor(this.color, -0.2));
            }
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
}
