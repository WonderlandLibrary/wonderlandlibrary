package cc.novoline.gui.button;

import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.api.FontRenderer;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

/**
 * @author xDelsy
 */
public class RoundedButton extends GuiButton {

    private final int radius;
    private final FontRenderer fontRenderer;

    public RoundedButton(String buttonText,
                         int id,
                         int x,
                         int y,
                         int width,
                         int height,
                         int radius,
                         @NotNull FontRenderer fontRenderer) {
        super(id, x, y, width, height, buttonText);
        this.radius = radius;
        this.fontRenderer = fontRenderer;
    }

    public RoundedButton(String buttonText,
                         int id,
                         int x,
                         int y,
                         int radius,
                         @NotNull FontRenderer fontRenderer) {
        this(buttonText, id, x, y, 200, 20, radius, fontRenderer);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(!visible) {
            return;
        }

        double xPosition = this.xPosition;
        double yPosition = this.yPosition;
        int width = this.width;
        int height = this.height;

        this.hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        int alpha = !hovered ? 75 : 100;

        RenderUtils.drawRoundedRect((int) xPosition,
                (int) yPosition + 1, width, height,
                radius,
                0x000000 | alpha << 24);
        mouseDragged(mc, mouseX, mouseY);

        fontRenderer.drawString(displayString, // @off
                (int) xPosition + (width - fontRenderer.stringWidth(displayString)) / 2.0F,
                (int) yPosition + 2 + (height - fontRenderer.getHeight()) / 2.0F,
                new Color(198, 198, 198).getRGB()); // @on
    }

    /** Отключение звука при клике. */
    @Override
    public void playPressSound(@NotNull SoundHandler soundHandler) {
        super.playPressSound(soundHandler);
    }

    //region Lombok
    public int getRadius() { return radius; }
    //endregion
}
