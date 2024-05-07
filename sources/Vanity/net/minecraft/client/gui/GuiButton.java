package net.minecraft.client.gui;

import com.masterof13fps.Client;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.awt.*;

public class GuiButton extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    public double cs = 0;
    public int alpha = 255;
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
    public String[] buttonDesc;
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

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String[] buttonDesc) {
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
        this.buttonDesc = buttonDesc;
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
        if (visible) {
            hovered = ((mouseX >= xPosition) && (mouseY >= yPosition)
                    && (mouseX < xPosition + width) && (mouseY < yPosition + height));
            updatefade();
            if (hovered) {
                if (cs >= 0.1) {
                    cs = 0.1;
                }
                cs += 0.1;
            } else {
                if (cs <= 0) {
                    cs = 0;
                }
                cs -= 1;
            }

            Color color1 = new Color(0.0F, 0.0F, 0.0F, alpha / 255.0F);
            int col1 = color1.getRGB();

            if (enabled) {
                RenderUtils.drawRoundedRect((float) (xPosition + cs), yPosition, (float) ((float) width - cs - 5), height - 5, 9, new Color(16, 16, 16).getRGB());
            } else {
                RenderUtils.drawRoundedRect((float) (xPosition + cs), yPosition, (float) ((float) width - cs - 5), height - 5, 9, new Color(103, 103, 103).getRGB());
            }

            mouseDragged(mc, mouseX, mouseY);
            int var6 = 14737632;
            String text = StringUtils.stripControlCodes(displayString);
            UnicodeFontRenderer fontRenderer = Client.main().fontMgr().font("Comfortaa", 18, Font.PLAIN);
            fontRenderer.drawStringWithShadow(text, xPosition - (fontRenderer.getStringWidth(text) / 2) + width / 2, yPosition + (height - 6) / 2, var6);
        }
    }

    public void updatefade() {
        if (enabled) {
            if (hovered) {
                alpha += 25;
                if (alpha >= 210) {
                    alpha = 210;
                }
            } else {
                alpha -= 25;
                if (alpha <= 120) {
                    alpha = 120;
                }
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
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
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

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
