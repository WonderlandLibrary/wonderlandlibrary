/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;

public class GuiButton
extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private static final HyperiumFontRenderer fr = Client.getFont();
    protected int width = 200;
    protected int height = 20;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected boolean hovered;

    private void drawRoundedRect(int x2, int y2, int width, int height, int cornerRadius, Color color) {
        Gui.drawRect(x2, y2 + cornerRadius, x2 + cornerRadius, y2 + height - cornerRadius, color.getRGB());
        Gui.drawRect(x2 + cornerRadius, y2, x2 + width - cornerRadius, y2 + height, color.getRGB());
        Gui.drawRect(x2 + width - cornerRadius, y2 + cornerRadius, x2 + width, y2 + height - cornerRadius, color.getRGB());
        this.drawArc(x2 + cornerRadius, y2 + cornerRadius, cornerRadius, 0, 90, color);
        this.drawArc(x2 + width - cornerRadius, y2 + cornerRadius, cornerRadius, 270, 360, color);
        this.drawArc(x2 + width - cornerRadius, y2 + height - cornerRadius, cornerRadius, 180, 270, color);
        this.drawArc(x2 + cornerRadius, y2 + height - cornerRadius, cornerRadius, 90, 180, color);
    }

    private void drawArc(int x2, int y2, int radius, int startAngle, int endAngle, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x2, y2, 0.0).endVertex();
        for (int i2 = (int)((double)startAngle / 360.0 * 100.0); i2 <= (int)((double)endAngle / 360.0 * 100.0); ++i2) {
            double angle = Math.PI * 2 * (double)i2 / 100.0 + Math.toRadians(180.0);
            worldRenderer.pos((double)x2 + Math.sin(angle) * (double)radius, (double)y2 + Math.cos(angle) * (double)radius, 0.0).endVertex();
        }
        Tessellator.getInstance().draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public GuiButton(int buttonId, int x2, int y2, String buttonText) {
        this(buttonId, x2, y2, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x2, int y2, int widthIn, int heightIn, String buttonText) {
        this.id = buttonId;
        this.xPosition = x2;
        this.yPosition = y2;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    protected int getHoverState(boolean mouseOver) {
        int i2 = 1;
        if (!this.enabled) {
            i2 = 0;
        } else if (mouseOver) {
            i2 = 2;
        }
        return i2;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i2 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawRoundedRect(this.xPosition, this.yPosition, this.width, this.height, 7, new Color(0, 0, 0, 150));
            this.mouseDragged(mc, mouseX, mouseY);
            int j2 = 0xE0E0E0;
            if (!this.enabled) {
                j2 = 0xA0A0A0;
            } else if (this.hovered) {
                j2 = 0xFFFFA0;
            }
            fr.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 10) / 2, j2);
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
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

