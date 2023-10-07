package dev.rise.ui.altmanager.gui.elements;

import dev.rise.font.CustomFont;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public final class RiseGuiButton extends GuiButton {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    /**
     * The x position of this control.
     */
    public int xPosition;
    /**
     * The y position of this control.
     */
    public int yPosition;
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

    boolean bg = true;

    public RiseGuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public RiseGuiButton(final int buttonId, final int x, final int y, final String buttonText, final boolean backGround) {
        this(buttonId, x, y, 200, 20, buttonText);
        bg = backGround;
    }

    public RiseGuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
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

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(final boolean mouseOver) {
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
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            if (bg)
                RenderUtil.roundedRect(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(255, 255, 255, 35));

            if (hovered && bg)
                RenderUtil.roundedRect(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(0, 0, 0, 35));

            this.mouseDragged(mc, mouseX, mouseY);

            CustomFont.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 8) / 2.0F, Color.WHITE.hashCode());
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(final int mouseX, final int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }

    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }
}
