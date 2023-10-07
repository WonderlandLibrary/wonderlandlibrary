package best.azura.client.impl.ui.chat;

import best.azura.client.impl.Client;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class CustomButton extends GuiButton {

    public CustomButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        RenderUtil.INSTANCE.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Integer.MIN_VALUE);
        this.mouseDragged(mc, mouseX, mouseY);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int j = 14737632;
        if (!this.enabled) {
            j = 10526880;
        } else if (this.hovered) {
            j = 16777120;
        }
        if (displayString.equals(GuiCustomChat.currentTab))
            j = ColorUtil.getLastHudColor().getRGB();
        this.drawCenteredString(mc.fontRendererObj, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {

    }
}