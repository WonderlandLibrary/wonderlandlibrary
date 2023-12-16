// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures;
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    
    static {
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }
    
    protected int getHoverState(final boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        }
        else if (mouseOver) {
            i = 2;
        }
        return i;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = new Color(255, 255, 255, 100).getRGB();
            final int j = new Color(255, 255, 255, 50).getRGB();
            final int k = new Color(150, 150, 150, 50).getRGB();
            final int l = this.enabled ? (this.hovered ? i : j) : k;
            final int i2 = this.enabled ? (this.hovered ? new Color(255, 255, 255, 150).getRGB() : new Color(255, 255, 255, 100).getRGB()) : new Color(150, 150, 150, 100).getRGB();
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, l);
            this.drawOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, i2);
            final int j2 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.mouseDragged(mc, mouseX, mouseY);
            int k2 = 14737632;
            if (!this.enabled) {
                k2 = 10526880;
            }
            else if (this.hovered) {
                k2 = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, k2);
        }
    }
    
    public void drawOutline(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, bottom, right, bottom + 1, color);
    }
    
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
    }
    
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
    
    public boolean isMouseOver() {
        return this.hovered;
    }
    
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }
    
    public int getButtonWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
