// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.awt.Font;
import net.augustus.ui.GuiIngameHook;
import net.augustus.utils.sound.SoundUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.augustus.ui.augustusmanager.AugustusSounds;
import net.minecraft.client.audio.SoundHandler;
import java.awt.Color;
import net.augustus.Augustus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.augustus.font.UnicodeFontRenderer;

public class GuiButton extends Gui
{
    private boolean lastHovered;
    private int anim;
    private int anim2;
    private static UnicodeFontRenderer fontrenderer;
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
    private boolean otherVisible;
    
    public GuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this.lastHovered = false;
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.otherVisible = true;
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
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
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
            Gui.drawRect(this.xPosition, this.yPosition, this.anim + this.xPosition, (int)(float)(this.height + this.yPosition), this.getHoverColor(Augustus.getInstance().getClientColor(), -0.2));
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
    
    public void drawBetterButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.otherVisible) {
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
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
            Gui.drawRect(this.xPosition, this.yPosition, this.anim + this.xPosition, (int)(float)(this.height + this.yPosition), this.getHoverColor(Augustus.getInstance().getClientColor(), -0.2));
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (!this.enabled) {
                j = 10526880;
            }
            else if (this.hovered) {
                j = 16777120;
            }
            GuiButton.fontrenderer.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 8) / 2), j);
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
    
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
    }
    
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && (this.visible || this.otherVisible) && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
    
    public boolean isMouseOver() {
        return this.hovered;
    }
    
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    public void playPressSound(final SoundHandler soundHandlerIn) {
        final String currentSound;
        final String var2 = currentSound = AugustusSounds.currentSound;
        switch (currentSound) {
            case "Vanilla": {
                soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                break;
            }
            case "Sigma": {
                SoundUtil.play(SoundUtil.button);
                break;
            }
        }
    }
    
    public int getButtonWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    static {
        GuiButton.fontrenderer = null;
        try {
            GuiButton.fontrenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/Comfortaa-Bold.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
}
