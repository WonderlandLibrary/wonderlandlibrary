package cc.novoline.gui.group;

import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.api.FontRenderer;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_35.SFBOLD_35;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public abstract class AbstractGroup extends Gui {

    protected final FontRenderer titleFontRenderer;
    protected final String title;
    protected boolean hidden;

    protected int xPosition;
    protected final int yPosition;
    protected final int width;
    protected final int height;

    protected AbstractGroup(@Nullable String title,
                            int xPosition,
                            int yPosition,
                            int width,
                            int height,
                            @NotNull FontRenderer titleFontRenderer) {
        this.title = title != null && !(title = title.trim()).isEmpty() ? title : null;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.titleFontRenderer = titleFontRenderer;
    }

    protected AbstractGroup(String title,
                            int xPosition,
                            int yPosition,
                            int width,
                            int height) {
        this(title, xPosition, yPosition, width, height, SFBOLD_35);
    }

    public void drawGroup(Minecraft mc, int mouseX, int mouseY) {
        if (this.hidden) return;

        RenderUtils.drawRect(this.xPosition, this.yPosition, this.width, this.height, 0xD2FFFFFF);

        if (this.title != null) {
            this.titleFontRenderer.drawString(this.title, // @off
                    this.xPosition + (this.width - this.titleFontRenderer.stringWidth(this.title)) / 2.0F,
                    this.yPosition + 4,
                    0); // @on
        }
    }

}
