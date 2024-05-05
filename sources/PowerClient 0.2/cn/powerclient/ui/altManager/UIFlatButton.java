/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.altManager;

import java.awt.Color;
import me.AveReborn.Client;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class UIFlatButton
extends GuiButton {
    private TimeHelper time = new TimeHelper();
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    private int color;
    private float opacity;
    private FontRenderer font;

    public UIFlatButton(int buttonId, int x2, int y2, int widthIn, int heightIn, String buttonText, int color) {
        super(buttonId, x2, y2, 10, 12, buttonText);
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x2;
        this.yPosition = y2;
        this.displayString = buttonText;
        this.color = color;
        this.font = Minecraft.getMinecraft().fontRendererObj;
    }

    public UIFlatButton(int buttonId, int x2, int y2, int widthIn, int heightIn, String buttonText, int color, FontRenderer font) {
        super(buttonId, x2, y2, 10, 12, buttonText);
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x2;
        this.yPosition = y2;
        this.displayString = buttonText;
        this.color = color;
        this.font = font;
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        int i2 = 1;
        if (!this.enabled) {
            i2 = 0;
        } else if (mouseOver) {
            i2 = 2;
        }
        return i2;
    }

    @Override
    public void drawButton(Minecraft mc2, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (!this.hovered) {
                this.time.reset();
                this.opacity = 0.0f;
            }
            if (this.hovered) {
                this.opacity += 0.5f;
                if (this.opacity > 1.0f) {
                    this.opacity = 1.0f;
                }
            }
            float radius = (float)this.height / 2.0f;
            this.drawRoundedRect((float)this.xPosition - this.opacity * 0.1f, (float)this.yPosition - this.opacity, (float)(this.xPosition + this.width) + this.opacity * 0.1f, (float)this.yPosition + radius * 2.0f + this.opacity, 1.0f, this.color);
            GL11.glColor3f(2.55f, 2.55f, 2.55f);
            this.mouseDragged(mc2, mouseX, mouseY);
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glScaled(1.0, 1.0, 1.0);
            int var6 = -1;
            float textHeight = this.font.FONT_HEIGHT;
            Client.fontManager.tahoma20.drawCenteredString(this.displayString, this.xPosition + this.width / 2 - 2, (int)((float)this.yPosition + (float)(this.height - this.font.FONT_HEIGHT) / 2.0f) - 2, this.hovered ? -1 : -3487030);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    private Color darkerColor(Color c2, int step) {
        int red = c2.getRed();
        int blue = c2.getBlue();
        int green = c2.getGreen();
        if (red >= step) {
            red -= step;
        }
        if (blue >= step) {
            blue -= step;
        }
        if (green >= step) {
            green -= step;
        }
        return c2.darker();
    }

    @Override
    protected void mouseDragged(Minecraft mc2, int mouseX, int mouseY) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc2, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    public int getButtonWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    public void drawRoundedRect(float x2, float y2, float x22, float y22, float round, int color) {
        x2 = (float)((double)x2 + ((double)(round / 2.0f) + 0.5));
        y2 = (float)((double)y2 + ((double)(round / 2.0f) + 0.5));
        x22 = (float)((double)x22 - ((double)(round / 2.0f) + 0.5));
        y22 = (float)((double)y22 - ((double)(round / 2.0f) + 0.5));
        UIFlatButton.drawRect(x2, y2, x22, y22, color);
        UIFlatButton.circle(x22 - round / 2.0f, y2 + round / 2.0f, round, color);
        UIFlatButton.circle(x2 + round / 2.0f, y22 - round / 2.0f, round, color);
        UIFlatButton.circle(x2 + round / 2.0f, y2 + round / 2.0f, round, color);
        UIFlatButton.circle(x22 - round / 2.0f, y22 - round / 2.0f, round, color);
        UIFlatButton.drawRect(x2 - round / 2.0f - 0.5f, y2 + round / 2.0f, x22, y22 - round / 2.0f, color);
        UIFlatButton.drawRect(x2, y2 + round / 2.0f, x22 + round / 2.0f + 0.5f, y22 - round / 2.0f, color);
        UIFlatButton.drawRect(x2 + round / 2.0f, y2 - round / 2.0f - 0.5f, x22 - round / 2.0f, y22 - round / 2.0f, color);
        UIFlatButton.drawRect(x2 + round / 2.0f, y2, x22 - round / 2.0f, y22 + round / 2.0f + 0.5f, color);
    }
}

