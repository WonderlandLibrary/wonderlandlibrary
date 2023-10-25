/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton
extends Gui {
    protected int width = 200;
    protected boolean hovered;
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    public int yPosition;
    public boolean enabled = true;
    public String displayString;
    public int id;
    public int xPosition;
    public boolean visible = true;
    protected int height = 20;

    public int getButtonWidth() {
        return this.width;
    }

    protected void mouseDragged(Minecraft minecraft, int n, int n2) {
    }

    public GuiButton(int n, int n2, int n3, String string) {
        this(n, n2, n3, 200, 20, string);
    }

    public void drawButtonForegroundLayer(int n, int n2) {
    }

    public void mouseReleased(int n, int n2) {
    }

    public void playPressSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public boolean mousePressed(Minecraft minecraft, int n, int n2) {
        return this.enabled && this.visible && n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
    }

    protected int getHoverState(boolean bl) {
        int n = 1;
        if (!this.enabled) {
            n = 0;
        } else if (bl) {
            n = 2;
        }
        return n;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public void drawButton(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            FontRenderer fontRenderer = Minecraft.fontRendererObj;
            minecraft.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
            int n3 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + n3 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + n3 * 20, this.width / 2, this.height);
            this.mouseDragged(minecraft, n, n2);
            int n4 = 0xE0E0E0;
            if (!this.enabled) {
                n4 = 0xA0A0A0;
            } else if (this.hovered) {
                n4 = 0xFFFFA0;
            }
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, n4);
        }
    }

    public GuiButton(int n, int n2, int n3, int n4, int n5, String string) {
        this.id = n;
        this.xPosition = n2;
        this.yPosition = n3;
        this.width = n4;
        this.height = n5;
        this.displayString = string;
    }
}

