package club.cloverhook.gui.components;

import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class TextButton extends GuiButton
{

    public TextButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            this.hovered = enabled && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            Draw.drawRectangle(this.xPosition - (this.hovered ? 1 : 0), this.yPosition - (this.hovered ? 1 : 0), this.xPosition + this.width + (this.hovered ? 1 : 0), this.yPosition + this.height + (this.hovered ? 1 : 0),
                    enabled ? ColorCreator.create(10, 10, 10, 150) : ColorCreator.create(10, 10, 10, 75));

            Fonts.f18.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2 - Fonts.f16.getStringHeight(this.displayString) / 2,
                    this.hovered ? ColorCreator.create(255,255,255, enabled ? 255 : 100) : ColorCreator.create(150, 150, 150, enabled ? 255 : 100));
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
