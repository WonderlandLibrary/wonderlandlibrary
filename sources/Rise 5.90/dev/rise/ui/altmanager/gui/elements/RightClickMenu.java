package dev.rise.ui.altmanager.gui.elements;

import dev.rise.font.CustomFont;
import dev.rise.ui.altmanager.AltAccount;
import dev.rise.util.math.MathUtil;
import dev.rise.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public final class RightClickMenu {
    private float x, y;

    private float width = 100, height = CustomFont.getHeight() + 4;

    private float hoverProgress;

    private boolean visible;

    private AltAccount selectedAlt;

    public RightClickMenu(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(final int mouseX, final int mouseY) {
        RenderUtil.roundedRect(x, y, width, height, 3, new Color(50, 50, 50, 255));

        if (isHovered(mouseX, mouseY)) {
            hoverProgress = MathUtil.lerp(hoverProgress, 100, 0.2f);
        } else {
            hoverProgress = MathUtil.lerp(hoverProgress, -1, 0.2f);
        }

        if (hoverProgress > 0.9)
            RenderUtil.roundedRect(x, y, hoverProgress, height, 3, new Color(78, 161, 253, 255));

        CustomFont.drawString("Delete", x + 1, y + 2, Color.WHITE.getRGB());
    }

    public boolean handleClick(final int mouseX, final int mouseY) {
        return isHovered(mouseX, mouseY);
    }

    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
