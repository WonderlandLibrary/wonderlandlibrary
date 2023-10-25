/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;

public class GuiButton
extends Gui {
    private int fade = 20;
    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    protected int width = 200;
    protected int height = 20;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Color color = new Color(this.fade, this.fade, this.fade, 255);
            Color text = new Color(255, 255, 255, 255);
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            if (!this.enabled) {
                color = new Color(10, 10, 10, 255);
                text = new Color(100, 100, 100, 255);
            } else if (this.hovered) {
                if (this.fade < 50) {
                    this.fade += 3;
                }
            } else if (this.fade > 20) {
                this.fade -= 3;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            RectHelper.drawRectBetter(this.xPosition - 2, this.yPosition, this.width + 2, this.height, ClientHelper.getClientColor().getRGB());
            RectHelper.drawRectBetter(this.xPosition, this.yPosition, this.width, this.height, color.getRGB());
            RenderHelper.renderBlurredShadow(this.xPosition - 2, (double)this.yPosition - 0.5, 3.0, (double)this.height + 0.5, 3, ClientHelper.getClientColor());
            RectHelper.drawRectBetter(this.xPosition, this.yPosition, this.width, this.height, new Color(0, 0, 0, 0).getRGB());
            mc.fontRenderer.drawCenteredString(this.displayString, (float)this.xPosition + (float)this.width / 2.0f, (float)this.yPosition + (float)(this.height - 8) / 2.0f, text.getRGB());
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

