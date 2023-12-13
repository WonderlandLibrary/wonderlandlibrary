package me.nyan.flush.ui.elements;

import net.minecraft.client.gui.Gui;

public class ProgressBar extends Gui {
    private final float x;
    private final float y;
    private final float width;
    private double value;

    public ProgressBar(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void draw(float partialTicks) {
        if (value < 2) {
            value += 0.25 * partialTicks;

            if (value >= 2) {
                value = 0;
            }
        }
        drawRect(x, y - 1, x + width, y + 1, 0xFF2D2D2D);
        double left = x + (width / 2) * value;
        double right = x + (width / 2) * value + width / 2;
        if (right > x + width) {
            drawRect(x, y - 1, x + right - (x + width), y + 1, 0xFFBB86FC);
            right = x + width;
        }
        drawRect(left, y - 1, right, y + 1, 0xFFBB86FC);
    }
}
