package cc.novoline.gui.group;

import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.api.FontRenderer;
import java.awt.*;
import net.minecraft.client.Minecraft;

/**
 * @author xDelsy
 */
public class GuiRoundedGroup extends AbstractGroup {

    /* fields */
    protected final int radius;

    /* constructors */
    public GuiRoundedGroup(String title, int xPosition, int yPosition, int width, int height, int radius,
                           FontRenderer titleFontRenderer) {
        super(title, xPosition, yPosition, width, height, titleFontRenderer);
        this.radius = radius;
    }

    public GuiRoundedGroup(String title, int xPosition, int yPosition, int width, int height, int radius) {
        super(title, xPosition, yPosition, width, height);
        this.radius = radius;
    }

    /* methods */
    @Override
    public void drawGroup(Minecraft mc, int mouseX, int mouseY) {
        if (this.hidden) return;

        RenderUtils.drawRoundedRect(this.xPosition,
                this.yPosition, this.width, this.height, this.radius, new Color(0, 0, 0,80).getRGB());

        if (this.title != null) {
            this.titleFontRenderer.drawString(this.title, // @off
                    this.xPosition + (this.width - this.titleFontRenderer.stringWidth(this.title)) / 2.0F,
                    this.yPosition + 4,
                    new Color(198, 198, 198).getRGB()); // @on
        }
    }

    //region Lombok
    public int getRadius() {
        return this.radius;
    }
    //endregion

}
