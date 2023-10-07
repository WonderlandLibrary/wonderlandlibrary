package net.minecraft.client.gui;

import cc.novoline.utils.ColorUtils;
import cc.novoline.utils.RenderUtils;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_19.SFTHIN_19;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GuiButton extends Gui {

    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    /**
     * The x position of this control.
     */
    public double xPosition;
    /**
     * The y position of this control.
     */
    public double yPosition;
    /**
     * The string displayed on this control.
     */
    public String displayString;
    public int id;
    /**
     * True if this control is enabled, false to disable.
     */
    public boolean enabled;
    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    /**
     * Button width in pixels
     */
    protected int width;
    /**
     * Button height in pixels
     */
    protected int height;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            final FontRenderer fontRenderer = mc.fontRendererObj;

            mc.getTextureManager().bindTexture(buttonTextures);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            final int i = getHoverState(this.hovered);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            final int rColor = this.hovered ? 0xFF2F2F2F : 0xFF2F3136;
            final int bColor = ColorUtils.getColor(0, 0, 0, this.hovered ? 140 : 180);

            if (mc.currentScreen instanceof GuiMainMenu) {
                RenderUtils.drawRoundedRect((int) this.xPosition, (int) this.yPosition + 1, this.width, 17, 8.0F, new Color(36, 36, 36, 255).getRGB());
            } else {
                drawTexturedModalRect((int) this.xPosition, (int) this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                drawTexturedModalRect((int) this.xPosition + this.width / 2, (int) this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            }

            mouseDragged(mc, mouseX, mouseY);
            final int textColor;

            if (this.hovered && this.enabled) {
                textColor = 0xFFD7D778;
            } else {
                textColor = 0xFFD7D7D7;
            }

            if (mc.currentScreen instanceof GuiMainMenu) {
                SFTHIN_19.drawCenteredString(this.displayString, (int) this.xPosition + this.width / 2F + 0.5F, (int) this.yPosition + (this.height - 6) / 2F, textColor, true);
            } else {
                this.drawCenteredString(fontRenderer, this.displayString, (int) (this.xPosition + this.width / 2 + 0.5), (int) this.yPosition + (this.height - 8) / 2, textColor);
            }
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(@NotNull SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public String getDisplayString() {
        return displayString;
    }
}
