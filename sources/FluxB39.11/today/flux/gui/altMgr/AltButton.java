package today.flux.gui.altMgr;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;

import java.awt.*;

public class AltButton {
    public int buttonID;

    public float x;
    public float y;
    public float width;
    public float height;
    public String displayString;
    public boolean isEnabled;

    public boolean isHovered = false;

    public AltButton(int buttonID, float x, float y, float width, float height, String displayStr) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = displayStr;

        isEnabled = true;
    }

    public void drawButton(int mouseX, int mouseY) {
        isHovered = RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + height);

        int buttonColor = !isEnabled ? GuiRenderUtils.darker(0xff9B59B6, 45) : 0xff9B59B6;
        GuiRenderUtils.drawBorderedRect(x, y, width, height, 0.5f, new Color(0x88000000, true).getRGB(),Color.WHITE.getRGB());

        FontManager.sans18.drawString(displayString, x + width / 2f - (FontManager.sans18.getStringWidth(displayString) / 2f), y + height / 2 - 7f, !isEnabled ? 0xffa19c9c : isHovered && isEnabled ? 0xff4286f5 : 0xffffffff);

        if(isEnabled && isHovered) {
            GuiRenderUtils.drawBorderedRect(x, y, width, height, 0.5f, new Color(0x22000000, true).getRGB(),0xff4286f5);
        }
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public boolean isHovered() {
        return isHovered;
    }
}
