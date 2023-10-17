package de.tired.base.guis.newclickgui.abstracts;

import de.tired.base.font.CustomFont;

public class ClickGuiUtils {

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    public int calculateMousePosition(int mouse, int position) {
        return mouse + position;
    }

}
