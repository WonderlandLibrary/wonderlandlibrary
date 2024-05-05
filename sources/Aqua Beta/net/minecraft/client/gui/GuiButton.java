package net.minecraft.client.gui;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.utils.ColorUtils;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton
extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected int width = 200;
    protected int height = 20;
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

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.blendFunc((int)770, (int)771);
            int fillColor = new Color(25, 25, 25, 120).hashCode();
            Color color1 = new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor());
            Color color2 = new Color(Aqua.setmgr.getSetting("HUDColor").getColor());
            Color finalColor = ColorUtils.getColorAlpha((Color)new Color(188, 130, 250), (int)170);
            RenderUtil.drawRoundedRect2Alpha((double)this.xPosition, (double)this.yPosition, (double)this.width, (double)this.height, (double)3.0, (Color)finalColor);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = this.hovered ? Color.darkGray.getRGB() : Color.WHITE.getRGB();
            Aqua.INSTANCE.comfortaa3.drawCenteredString(this.displayString, (float)this.xPosition + (float)this.width / 2.0f, (float)this.yPosition + (float)(this.height - 13) / 2.0f + 2.0f, j);
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
        soundHandlerIn.playSound((ISound)PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
